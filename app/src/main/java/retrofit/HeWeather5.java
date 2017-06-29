package retrofit;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/6/30.
 */
public class HeWeather5 {
    private Basic basic;

    private String status;

    public void setBasic(Basic basic){
        this.basic = basic;
    }
    public Basic getBasic(){
        return this.basic;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }

    @Override
    public String toString() {
        return "HeWeather5{" +
                "basic=" + basic +
                ", status='" + status + '\'' +
                '}';
    }
}
