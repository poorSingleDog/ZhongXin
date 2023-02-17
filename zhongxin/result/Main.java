package zhongxin;

import java.util.*;

public class Main {
    public static final double d=125;   //机机通讯距离
    public static final double D=70;    //基站和机通讯距离
    public static final double H=10;    //飞机高度
    public static final double v=5;     //飞机速度
    public static final double d1=90;   //飞机前后距离
    public static final double d2=80;   //轨道间距
    public static final double tf=0.1;  //转发时延
//    class station{
//        public double x;
//        public double y;
//        public double z;
//        public station(double x,double y,double z){
//            this.x=x;
//            this.y=y;
//            this.z=z;
//        }
//        public void addTime(double t){
//            x-=5*t;
//        }
//    }
    public int[][] choices(double x,double y){     //x,y是基站的坐标
        //无人机坐标(90m,80n)
        List<Integer> temp=new ArrayList<>();   //公式=m+n*100000
        int base_m= (int) (x/d1);
        int base_n= (int) (y/d2);
        int range=2;    //看base-2到base+2的范围中哪些无人机在基站连接距离内
        for(int i=base_m-range;i<base_m+range+1;i++){
            for(int j=base_n-range;j<base_n+range+1;j++){
                if((d1*i-x)*(d1*i-x)+(d2*j-y)*(d2*j-y)<=D*D){
                    temp.add(i+100000*j);
                }
            }
        }
        int[][] res=new int[temp.size()][2];
        for(int i=0;i<temp.size();i++){
            res[i][0]=temp.get(i)%100000;
            res[i][1]=temp.get(i)/100000;
        }
        return res;
    }

    public static void main(String[] args) {
        double[] startT={0,4.7,16.4};
        Main test=new Main();
        //从起点到基站1

        //-------------------------------------------------信号到达第一个飞机上前以基站为参考系--------------------------------------------------------
        double[] intersection1=new double[2];      //与圆的右上交点坐标(105.6727,80),圆心(45.73,45.26)
        intersection1[0]=105.6727;
        intersection1[1]=80;
        double[] intersection2=new double[2];      //与圆的左上交点坐标
        intersection2[0]=-14.2127;
        intersection2[1]=80;
        //信号从基站发出的那一刻，飞机所在的位置
        double[] start_plane=new double[2];
        start_plane[0]=intersection1[0]-v*(0.1+69.28203/10000);
        start_plane[1]=intersection1[1];
        for(int i=0;i<startT.length;i++){
            double T=startT[i];
            double S_T=startT[i];
            double[] start=new double[3];
            start[0]=45.73;
            start[1]=45.26;
            start[2]=0;
            double[] end1=new double[3];
            end1[0]=1200;
            end1[1]=700;
            end1[2]=0;
            double[] end2=new double[3];
            end2[0]=-940;
            end2[1]=1100;
            end2[2]=0;
            //找到T的情况下用以派往end1的飞机编号m和n，(交点1左边的第一个飞机)
            List<Integer> planes_m=new ArrayList<>();
            List<Integer> planes_n=new ArrayList<>();
            List<Double> times=new ArrayList<>();
            if(i==0){
                planes_m.add(1);
                planes_n.add(1);
                double dis=Math.sqrt((90-45.73)*(90-45.73)+(80-45.26)*(80-45.26)+100);
                T+=tf+dis/10000;
                times.add(T);
            }else if(i==1){
                planes_m.add(0);
                planes_n.add(1);
                double dis=Math.sqrt(22.23*22.23+(80-45.26)*(80-45.26)+100);
                T+=tf+dis/10000;
                times.add(T);
            }else {
                planes_m.add(0);
                planes_n.add(1);
                double dis=Math.sqrt(36.27*36.27+(80-45.26)*(80-45.26)+100);
                T+=tf+dis/10000;
                times.add(T);
            }
//            if(start_plane[0]-v*T>=0){
//                int m= (int) ((start_plane[0]-v*T)/d1);
//                planes_m.add(m);
//                planes_n.add(1);
//            }else {
//                int m= (int) ((start_plane[0]-v*T)/d1)-1;
//                planes_m.add(m);
//                planes_n.add(1);
//            }
//            double deta_t=(start_plane[0]-(T*v+planes_m.get(0)*d1))/v;     //基站等待时间
//            double start_time=T+deta_t;
//            times.add(T+tf+69.28203/10000+deta_t);
//            T+=tf+69.28203/10000+deta_t;

            //找到T的情况下用以派往end2的飞机编号m和n，(交点2左边的第一个飞机)
            List<Integer> S_planes_m=new ArrayList<>();
            List<Integer> S_planes_n=new ArrayList<>();
            List<Double> S_times=new ArrayList<>();
            if(i==0){
                S_planes_m.add(0);
                S_planes_n.add(1);
                double dis=Math.sqrt(45.73*45.73+(80-45.26)*(80-45.26)+100);
                S_T+=tf+dis/10000;
                S_times.add(S_T);
            }else if(i==1){
                S_planes_m.add(0);
                S_planes_n.add(1);
                double dis=Math.sqrt(22.23*22.23+(80-45.26)*(80-45.26)+100);
                S_T+=tf+dis/10000;
                S_times.add(S_T);
            }else {
                S_planes_m.add(-1);
                S_planes_n.add(1);
                double dis=Math.sqrt((90-36.27)*(90-36.27)+(80-45.26)*(80-45.26)+100);
                S_T+=tf+dis/10000;
                S_times.add(S_T);

            }
//            S_planes_m.add((int)((intersection2[0]-v*S_T)/d1)-1);
//            S_planes_n.add(1);
//            double S_deta_t=(intersection2[0]-(S_T*v+S_planes_m.get(0)*d1))/v;     //基站等待时间
//            double S_start_time=S_T+S_deta_t;
//            S_times.add(S_T+tf+69.28203/10000+S_deta_t);
//            S_T+=tf+69.28203/10000+S_deta_t;

            //-------------------------------------------------信号到达第一个飞机上后以飞机为参考系-----------------------------------------------------

            //去向end1的信号
            end1[0]-=v*times.get(0);
            int temp_m=planes_m.get(0);
            int temp_n=planes_n.get(0);
            //当信号与end1的距离大于传输距离，寻找下一个飞机
            while ((temp_m*90-end1[0])*(temp_m*90-end1[0])+(temp_n*80-end1[1])*(temp_n*80-end1[1])>4800){
                Map<Integer,Double> next=new HashMap<>();   //方向--与终点的距离
                int p=0;
                for(int j=-1;j<2;j++){
                    for(int k=-1;k<2;k++){
                        if(j==0&&k==0)
                            continue;
                        double dis=(end1[0]-(temp_m+j)*90)*(end1[0]-(temp_m+j)*90)+(end1[1]-(temp_n+k)*80)*(end1[1]-(temp_n+k)*80);
                        next.put(p++,dis);
                    }
                }
                List<Map.Entry<Integer,Double>> Next=new ArrayList<>(next.entrySet());
                Collections.sort(Next, new Comparator<Map.Entry<Integer, Double>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                        if(o2.getValue()-o1.getValue()<0)
                            return 1;
                        else if(o2.getValue()-o1.getValue()>0)
                            return -1;
                        else
                            return 0;
                    }
                });
                double deta=0;
                if(Next.get(0).getKey()==0){
                    temp_m-=1;
                    temp_n-=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==1){
                    temp_m-=1;
                    deta=tf+90.0/10000;
                }else if(Next.get(0).getKey()==2){
                    temp_m-=1;
                    temp_n+=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==3){
                    temp_n-=1;
                    deta=tf+80.0/10000;
                }else if(Next.get(0).getKey()==4){
                    temp_n+=1;
                    deta=tf+80.0/10000;
                }else if(Next.get(0).getKey()==5){
                    temp_m+=1;
                    temp_n-=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==6){
                    temp_m+=1;
                    deta=tf+90.0/10000;
                }else if(Next.get(0).getKey()==7){
                    temp_m+=1;
                    temp_n+=1;
                    deta=tf+120.415946/10000;
                }
                T+=deta;
                planes_m.add(temp_m);
                planes_n.add(temp_n);
                times.add(T);
                end1[0]-=deta*v;
            }

            //去向end2的信号
            end2[0]-=v*S_times.get(0);
            int S_temp_m=S_planes_m.get(0);
            int S_temp_n=S_planes_n.get(0);
            //当信号与end1的距离大于传输距离，寻找下一个飞机
            while ((S_temp_m*90-end2[0])*(S_temp_m*90-end2[0])+(S_temp_n*80-end2[1])*(S_temp_n*80-end2[1])>4800){
                Map<Integer,Double> next=new HashMap<>();   //方向--与终点的距离
                int p=0;
                for(int j=-1;j<2;j++){
                    for(int k=-1;k<2;k++){
                        if(j==0&&k==0)
                            continue;
                        double dis=(end2[0]-(S_temp_m+j)*90)*(end2[0]-(S_temp_m+j)*90)+(end2[1]-(S_temp_n+k)*80)*(end2[1]-(S_temp_n+k)*80);
                        next.put(p++,dis);
                    }
                }
                List<Map.Entry<Integer,Double>> Next=new ArrayList<>(next.entrySet());
                Collections.sort(Next, new Comparator<Map.Entry<Integer, Double>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                        if(o2.getValue()-o1.getValue()<0)
                            return 1;
                        else if(o2.getValue()-o1.getValue()>0)
                            return -1;
                        else
                            return 0;
                    }
                });
                double deta=0;
                if(Next.get(0).getKey()==0){
                    S_temp_m-=1;
                    S_temp_n-=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==1){
                    S_temp_m-=1;
                    deta=tf+90.0/10000;
                }else if(Next.get(0).getKey()==2){
                    S_temp_m-=1;
                    S_temp_n+=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==3){
                    S_temp_n-=1;
                    deta=tf+80.0/10000;
                }else if(Next.get(0).getKey()==4){
                    S_temp_n+=1;
                    deta=tf+80.0/10000;
                }else if(Next.get(0).getKey()==5){
                    S_temp_m+=1;
                    S_temp_n-=1;
                    deta=tf+120.415946/10000;
                }else if(Next.get(0).getKey()==6){
                    S_temp_m+=1;
                    deta=tf+90.0/10000;
                }else if(Next.get(0).getKey()==7){
                    S_temp_m+=1;
                    S_temp_n+=1;
                    deta=tf+120.415946/10000;
                }
                S_T+=deta;
                S_planes_m.add(S_temp_m);
                S_planes_n.add(S_temp_n);
                S_times.add(S_T);
                end2[0]-=deta*v;
            }
            double finish_time=T+tf+Math.sqrt((planes_m.get(planes_m.size()-1)*90-end1[0])*(planes_m.get(planes_m.size()-1)*90-end1[0])+(planes_n.get(planes_n.size()-1)*80-end1[1])*(planes_n.get(planes_n.size()-1)*80-end1[1])+100)/10000;
            double S_finish_time=S_T+tf+Math.sqrt((S_planes_m.get(S_planes_m.size()-1)*90-end2[0])*(S_planes_m.get(S_planes_m.size()-1)*90-end2[0])+(S_planes_n.get(S_planes_n.size()-1)*80-end2[1])*(S_planes_n.get(S_planes_n.size()-1)*80-end2[1])+100)/10000;
//            for(int n=0;n<planes_m.size();n++){
//                System.out.println(planes_m.get(n)+"\t"+planes_n.get(n)+"\t"+times.get(n));
//            }
//            System.out.println("begin:"+start_time+"\t"+"end:"+finish_time);
//            System.out.println();
//
//            for(int n=0;n<S_planes_m.size();n++){
//                System.out.println(S_planes_m.get(n)+"\t"+S_planes_n.get(n)+"\t"+S_times.get(n));
//            }
//            System.out.println("begin:"+S_start_time+"\t"+"end:"+S_finish_time);
//            System.out.println();

            System.out.println(String.format("%.4f",startT[i])+",0,1,"+String.format("%.4f",finish_time-startT[i])+",1");
            for(int n=0;n<planes_m.size();n++){
                System.out.print("("+String.format("%.4f",times.get(n))+","+planes_m.get(n)+","+planes_n.get(n)+")");
                if(n<planes_m.size()-1){
                    System.out.print(",");
                }
            }
            System.out.println();

            System.out.println(String.format("%.4f",startT[i])+",0,2,"+String.format("%.4f",S_finish_time-startT[i])+",1");
            for(int n=0;n<S_planes_m.size();n++){
                System.out.print("("+String.format("%.4f",S_times.get(n))+","+S_planes_m.get(n)+","+S_planes_n.get(n)+")");
                if(n<S_planes_m.size()-1){
                    System.out.print(",");
                }
            }
            System.out.println();

        }

    }
}
