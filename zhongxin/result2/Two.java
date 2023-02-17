package zhongxin;

import java.util.*;

public class Two {
    //站台0~7
    public static final double[][] platform=new double[][]{{-614,1059,24},{-934,715,12},{1073,291,37},{715,129,35},{186,432,21},{-923,632,37},{833,187,24},{-63,363,11}};

    public List<plane> accessible_planes(double x,double y,double z,double t,double dis){    // dis=115或70
        List<plane> res=new ArrayList<>();
        int base_m= (int) ((x-5*t)/90);
        int base_n=(int)(y/80);
        int range=2;
        for(int i=base_m-range;i<base_m+range+1;i++){
            for(int j=base_n-range;j<base_n+range+1;j++){
                if((90*i+5*t-x)*(90*i+5*t-x)+(80*j-y)*(80*j-y)+(z-10)*(z-10)<=dis*dis){
                    res.add(new plane(i,j));
                }
            }
        }
        for(int i=0;i<platform.length;i++){
            if((platform[i][0]-x)*(platform[i][0]-x)+(platform[i][1]-y)*(platform[i][1]-y)+(platform[i][2]-z)*(platform[i][2]-z)<=dis*dis){
                res.add(new plane(i));
            }
        }
        return res;

    }
    public int judge(double x){
        if(x==45.73){
            return 0;
        }else if(x==1200){
            return 1;
        }else if(x==-940){
            return 2;
        }else {
            return -1;
        }
    }
    public void find_path(double start_x,double start_y,double end_x,double end_y,double begin_t){    //在开始时间为t的情况下，从出发点到结束点的路径(4种)

        List<step> path=new ArrayList<>();
        for(int i=0;i<4;i++) {          //3+3+3+1
            double T=begin_t+i*0.112;
            plane temp_plane=new plane(-1);     //起点约定为plane(-1)
            double temp_x=start_x;
            double temp_y=start_y;
            double temp_z=0;
            Map<Double, plane> way = new TreeMap<>();     //存储走过的路径<时刻-飞机>
            while ((temp_x - end_x) * (temp_x - end_x) + (temp_y - end_y) * (temp_y - end_y) + temp_z * temp_z > 70 * 70) {
                List<plane> choices;
                if(temp_plane.l==-1){
                    choices = accessible_planes(temp_x, temp_y, 0, T, 70);
                }else {
                    choices = accessible_planes(temp_x, temp_y, temp_z, T, 115);
                }

                Map<Double,plane> dis_plane=new TreeMap<>();
                for(plane p:choices){
                    double dis;     //距离终点的距离
                    if (p.isPlane) {
                        dis = (p.m * 90 + 5 * T - end_x) * (p.m * 90 + 5 * T - end_x) + (p.n * 80 - end_y) * (p.n * 80 - end_y) + 10 * 10;
                    } else {
                        dis = (platform[p.l][0] - end_x) * (platform[p.l][0] - end_x) + (platform[p.l][1] - end_y) * (platform[p.l][1] - end_y) + platform[p.l][2] * platform[p.l][2];
                    }
                    dis_plane.put(dis,p);
                }
                double originalT=T;
                l:for(Map.Entry<Double,plane> entry:dis_plane.entrySet()){      //看下一步选哪架范围内的飞机，选中即break
                    for(step s:path){
                        //若该条路负载已满
                        if(s.p1.equals(temp_plane) && s.p2.equals(entry.getValue()) && s.t1-0.00001<=T && T<s.t2){
                            continue l;
                        }
                    }
                    double next_x ;
                    double next_y ;
                    double next_z ;
                    if(entry.getValue().isPlane) {
                        next_x = entry.getValue().m * 90 + 5 * T;
                        next_y = entry.getValue().n * 80;
                        next_z = 10;
                    }else {
                        next_x=platform[entry.getValue().l][0];
                        next_y=platform[entry.getValue().l][1];
                        next_z=platform[entry.getValue().l][2];
                    }
                    double trans=Math.sqrt((temp_x-next_x)*(temp_x-next_x)+(temp_y-next_y)*(temp_y-next_y)+(temp_z-next_z)*(temp_z-next_z));
//                    T+=0.1+trans/10000;
                    way.put(T+0.1+trans/10000,entry.getValue());   //确定了接收的节点
                    if(entry.getValue().isPlane) {
                        temp_x = entry.getValue().m * 90 + 5 * (T+0.1+trans/10000);
                        temp_y = entry.getValue().n * 80;
                        temp_z = 10;
                    }else {
                        temp_x=platform[entry.getValue().l][0];
                        temp_y=platform[entry.getValue().l][1];
                        temp_z=platform[entry.getValue().l][2];
                    }
                    //起点plane，终点plane，开始时间，到达时间
                    path.add(new step(temp_plane,entry.getValue(),T,T+0.1+trans/10000));
                    temp_plane=entry.getValue();
                    T+=0.1+trans/10000;

                    break l;

                }
                //若没有一个plane可选，则等待至一条路负载为空
                if(originalT==T){
                    plane chosen_p=temp_plane;
                    List<step> common_start_step=new ArrayList<>();
                    double nearestT=9999;
                    for(step s:path){
                        if(s.p1.equals(temp_plane)){
                            common_start_step.add(s);
                        }
                    }
                    //按照结束时间从早到晚
                    Collections.sort(common_start_step, new Comparator<step>() {
                        @Override
                        public int compare(step o1, step o2) {
                            if(o1.t2>o2.t2){
                                return 1;
                            }else if(o1.t2<o2.t2){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    p:for(step s:common_start_step){
                        for(step other:common_start_step){
//                            if(s.p2.equals(other.p2) && other.t1==s.t2){
                            if(s.p2.equals(other.p2) && (other.t1==s.t2 || (other.t1<s.t2+0.11 && s.t2+0.11<other.t2))){
//                                System.out.println("二次冲突！");
                                continue p;
                            }
                        }
                        //没有任何二次冲突
                        chosen_p=s.p2;
                        nearestT=s.t2;
                        break ;
                    }

                    T=nearestT;
                    temp_x=temp_plane.m*90+5*T;     //更新当前飞机的x坐标
                    double next_x;
                    double next_y;
                    double next_z;
                    if (chosen_p.isPlane) {
                        next_x = chosen_p.m * 90 + 5 * T;
                        next_y = chosen_p.n * 80;
                        next_z = 10;
                    } else {
                        next_x=platform[chosen_p.l][0];
                        next_y=platform[chosen_p.l][1];
                        next_z=platform[chosen_p.l][2];
                    }
                    double trans=Math.sqrt((temp_x-next_x)*(temp_x-next_x)+(temp_y-next_y)*(temp_y-next_y)+(temp_z-next_z)*(temp_z-next_z));

                    if(chosen_p.isPlane) {
                        temp_x = chosen_p.m * 90 + 5 * (T+0.1+trans/10000);
                        temp_y = chosen_p.n * 80;
                        temp_z = 10;
                    }else {
                        temp_x=platform[chosen_p.l][0];
                        temp_y=platform[chosen_p.l][1];
                        temp_z=platform[chosen_p.l][2];
                    }
                    way.put(T+0.1+trans/10000,chosen_p);
                    path.add(new step(temp_plane,chosen_p,T,T+0.1+trans/10000));
                    T+=0.1+trans/10000;
                    temp_plane=chosen_p;

                }
            }

            //------------------------------从最后一个无人机到达终点，终点约定为plane(100)
            //记得更新temp_x
            boolean wait=false;
            for(step s:path){
                //若该条路负载已满
                if(s.p1.equals(temp_plane) && s.t1-0.00001<=T && T<s.t2){
                    wait=true;
                    break;
                }
            }
            if(!wait){
                double dis=(temp_x - end_x) * (temp_x - end_x) + (temp_y - end_y) * (temp_y - end_y) + temp_z * temp_z;
                path.add(new step(temp_plane,new plane(100),T,T+0.1+Math.sqrt(dis)/10000));
                T+=0.1+Math.sqrt(dis)/10000;           //到达终点的时间
            }else {
                //要改。。。
                List<step> common_start_step=new ArrayList<>();
                for(step s:path){
                    if(s.p1==temp_plane){
                        common_start_step.add(s);
                    }
                }
                Collections.sort(common_start_step, new Comparator<step>() {
                    @Override
                    public int compare(step o1, step o2) {
                        if(o1.t2>o2.t2){
                            return 1;
                        }else if(o1.t2<o2.t2){
                            return -1;
                        }else {
                            return 0;
                        }
                    }
                });
                double nearestT=9999;
                g:for(step s:common_start_step){
                    for(step other:common_start_step){
//                            if(s.p2.equals(other.p2) && other.t1==s.t2){
                        if( other.t1==s.t2 || (other.t1<s.t2+0.107 && s.t2+0.107<other.t2)){
//                            System.out.println("末尾二次冲突！");
                            continue g;
                        }
                    }
                    //没有任何二次冲突
                    nearestT=s.t2;
                    break ;
                }
                T=nearestT;         //信号开始传输时间
                temp_x=temp_plane.m*90+5*T;
                double trans=Math.sqrt((temp_x - end_x) * (temp_x - end_x) + (temp_y - end_y) * (temp_y - end_y) + temp_z * temp_z);
                path.add(new step(temp_plane,new plane(100),T,T+0.1+trans/10000));
                T+=0.1+trans/10000;

            }
            System.out.println(begin_t+","+judge(start_x)+","+judge(end_x)+","+String.format("%.4f",(T-begin_t))+","+(i==3?1:3));
            int num=0;
            for(Map.Entry<Double,plane> entry:way.entrySet()){
                if(entry.getValue().isPlane) {
                    System.out.print("("+String.format("%.4f", entry.getKey()) + "," +entry.getValue().m+","+entry.getValue().n+")");
                }else {
                    System.out.print("("+String.format("%.4f", entry.getKey()) + "," +entry.getValue().l+")");
                }
                if(++num<way.size()){
                    System.out.print(",");
                }
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        Two t=new Two();
        double[] startT={0,4.7,16.4};
        for(double beginT:startT){
            //1->2
            //1->3
            //2->1
            //2->3
            //3->1
            //3->2

            t.find_path(45.73,45.26,1200,700,beginT);
            t.find_path(45.73,45.26,-940,1100,beginT);
            t.find_path(1200,700,45.73,45.26,beginT);
            t.find_path(1200,700,-940,1100,beginT);
            t.find_path(-940,1100,45.73,45.26,beginT);
            t.find_path(-940,1100,1200,700,beginT);
        }


    }
}
class plane{
    public int m;
    public int n;
    public boolean isPlane;
    public int l;
    public plane(int m,int n){
        this.m=m;
        this.n=n;
        this.isPlane=true;
    }
    public plane(int l){
        this.l=l;
        this.isPlane=false;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof plane) {
            //强制转换成Person类型
            plane p = (plane) obj;
            //判断他们的属性值    注：这里的age为什么要用==？可以在评论区回答
            if (this.m==p.m && this.n == p.n && this.l==p.l) {
                return true;
            }
        }
        return false;
    }
}
class step{
    public plane p1;
    public plane p2;
    public double t1;
    public double t2;
    public step(plane p1,plane p2,double t1,double t2){
        this.p1=p1;
        this.p2=p2;
        this.t1=t1;
        this.t2=t2;
    }
}
