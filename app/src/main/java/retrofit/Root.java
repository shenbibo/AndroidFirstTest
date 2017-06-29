package retrofit;

import java.util.List;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/6/30.
 */
public class Root {
    private List<HeWeather5> HeWeather5 ;

    public void setHeWeather5(List<HeWeather5> HeWeather5){
        this.HeWeather5 = HeWeather5;
    }
    public List<HeWeather5> getHeWeather5(){
        return this.HeWeather5;
    }

    @Override
    public String toString() {
        return "Root{" +
                "HeWeather5=" + HeWeather5 +
                '}';
    }
}
