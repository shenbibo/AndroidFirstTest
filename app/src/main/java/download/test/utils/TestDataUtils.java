package download.test.utils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import download.test.bean.DataProvider;
import download.test.bean.DownloadFileInfo;
import download.test.view.DownloadListItemView;
import download.test.view.ProgressView;
import download.test.viewbeans.ProgressBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 下午2:06:31
 * @version
 * @since
 */
public class TestDataUtils
{
    /**
     * */
    public static final Map<String, DownloadFileInfo> TASK_MAP = new ConcurrentHashMap<String, DownloadFileInfo>();

    /**
     * 从网络获取图片的URL。
     * */
    public static final String[] TEST_ICON_URL =
    {
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/e2463499941c47afa22d8319f4822ea8.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/5408cfff87b24f5683f931ad667244ed.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/8822eeae7af44b2e9bbba08c63dbf896_1.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/8cf7d7ba9f6b4df88370e990d42e562a_1.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/acbc2dc252094871aae948a85e6be198.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/47ba83ab2b7e44b2a53f8af20527af5f.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/3177529ab36f45c1afb1af29e3e82dfd.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/86c603015c524e808c7de655ea7335de.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/9b5a3ded781e44d8854f24e2d9eabf3c.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/3c1f33db6a3746b985b157cb29c5e941.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/29ce74b0b9b84ed7b3a6e2cf9aa597d6.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/eced39cae0714a9a943ef085d8818266.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/7b48266d36764c4389a15de57e766bc7.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/d4e5916a5dc249e5b6f6878024981f14.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/de3b83ce63b84dea8e0c2b7e18ffd8d7.png",
            "http://appimg.hicloud.com/hwmarket/files/application/icon144/134a0ee7951b43bdba88bb0998875f0d.png",
            "http://appimg.hicloud.com/hwmarket/files/lapp/054c61d8374b4542b89a382c71a0d7a7/1441172074655image4.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/44547c6598174579bf9172ab4e89a24b/1441854475231image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/cb04f9e2718c4c7ead88e77bc8f8f6c7/1441698890384image1.png",
            "http://appimg.hicloud.com/hwmarket/files/lapp/00b06b084ed140a9b6116252c230e420/1441694152872image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/6ed151f1cebe4c459d5acf423081fd20/1441188981244image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/054c61d8374b4542b89a382c71a0d7a7/1441172074655image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/3383ff8485d04a2e8c37af40f645cdf7/1441170670487image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/1d3ccaa735c5437fbeda794af3bfb3f9/1441158701539image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/26bbd0a5491c45f48621be24f8bb0b8a/1441158688602image1.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/44547c6598174579bf9172ab4e89a24b/1441854475231image2.jpg",
            "http://appimg.hicloud.com/hwmarket/files/lapp/cb04f9e2718c4c7ead88e77bc8f8f6c7/1441698890384image2.png",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/7fb1ddee9c834011b532de822ebf28fb/1441159641730image1.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/487842ab833b4a1dae18b773f8f79325/1441107004799image1.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/865d66480f3b4bb1a898cd1b618fa482/1441099039344image1.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/67ee4a3503074243ade859018de329d6/1441038192739image1.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/e29a6157c8574441b45f152159c4f2b9/1441017268783image1.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/7362aa0a8dfe4619aa277111a1d5de8c/1441004460486image1.png",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/7fb1ddee9c834011b532de822ebf28fb/1441159641730image2.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/487842ab833b4a1dae18b773f8f79325/1441107004799image2.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/865d66480f3b4bb1a898cd1b618fa482/1441099039344image2.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/67ee4a3503074243ade859018de329d6/1441038192739image2.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/e29a6157c8574441b45f152159c4f2b9/1441017268783image2.jpg",
            "http://hispaceclt1.hicloud.com:8080/hwmarket3/files/lapp/7362aa0a8dfe4619aa277111a1d5de8c/1441004460486image2.png"

    };

    /**
     * 用于下载的url数据
     * */
    public static final String[][] TEST_DOWNLOAD_FILE_URL =
    { 
        {"唯品会", "21.98M", "http://122.11.38.214/dl/appdl/application/apk/59/59d650d1e1064453897802048a60d366/com.achievo.vipshop.1511171434.apk"},
        {"唯品会", "21.9M", "http://122.11.38.214/dl/appdl/application/apk/59/59d650d1e1064453897802048a60d366/com.achievo.vipshop.1511171434.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=10685&listId=2&position=1&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=a7537156cbc5b8d3785ed54e0ffac3b3%3BcdrInfo%3A201512011133251551969%5E3%5E16132%5E9.0%5E20637%5E10218%5E10685%5ECPA%5E2a74fad8d97011e292cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E9.0%5E30%5E1009136%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AA%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"美团", "12.9M", "http://122.11.38.214/dl/appdl/application/apk/8c/8cde9e8caa0b4460ab9122fccba2d4e6/com.sankuai.meituan.1511271932.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=7482&listId=2&position=2&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=a7537156cbc5b8d3785ed54e0ffac3b3%3BcdrInfo%3A201512011133251551970%5E3%5E12555%5E5.11%5E20532%5E6997%5E7482%5ECPA%5E4c437759d97211e292cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E5.11%5E30%5E1005206%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AA%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"高铁管家", "8.5M", "http://122.11.38.214/dl/appdl/application/apk/18/18e14c59416e4649b68ea48755ceb83a/com.gtgj.view.1511251459.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=9230&listId=2&position=6&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=a7537156cbc5b8d3785ed54e0ffac3b3%3BcdrInfo%3A201512011133251551972%5E3%5E14598%5E8.0%5E21154%5E8745%5E9230%5ECPA%5E1a3d639edc9511e292cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E8.0%5E30%5E10017070%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AA%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"铃声多多", "6M", "http://122.11.38.214/dl/appdl/application/apk/90/90689ce106b3472085bc776c32cdc1ee/com.shoujiduoduo.ringtone.1511271733.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=autoList&subsource=101%3B25&listId=2&position=25&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=540a8dfd5702bcd0466b541ed7dbbf12%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"中粮我买网", "4.4M", "http://122.11.38.214/dl/appdl/application/apk/fd/fdaf4173c662486ab4130f6d39e2edf6/com.womai.1511041808.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=7546&listId=2&position=44&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=f1ba812f90f78e1b8a4536747b22f95b%3BcdrInfo%3A201512011133251551991%5E3%5E12623%5E18.0%5E20949%5E7061%5E7546%5ECPA%5E1328994d3d1c11e392cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E18.0%5E30%5E1047665%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AC%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"寺库奢侈品", "4.5M", "http://122.11.38.214/dl/appdl/application/apk/56/5684c10865f74d28a0a207bd5c228f86/com.secoo.1511231533.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=6443&listId=2&position=46&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=f1ba812f90f78e1b8a4536747b22f95b%3BcdrInfo%3A201512011133251551992%5E3%5E11368%5E16.5%5E20826%5E5958%5E6443%5ECPA%5E1e523e883d1c11e392cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E16.5%5E30%5E10188248%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AC%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"WIFI密码查看器", "2.6M", "http://122.11.38.214/dl/appdl/application/apk/58/587f58c5b8cd43a1b57201bc66cafc5c/com.jason.guoxunkeji.1510131317.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=autoList&subsource=101%3B51&listId=2&position=51&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=f1ba812f90f78e1b8a4536747b22f95b%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"爱奇艺视频", "10.5M", "http://122.11.38.214/dl/appdl/application/apk/e4/e4dea8779ec54efea6eeeadf73394671/com.qiyi.video.1511231707.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=autoList&subsource=101%3B55&listId=2&position=55&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=f1ba812f90f78e1b8a4536747b22f95b%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"乐蜂网", "5.9M", "http://122.11.38.214/dl/appdl/application/apk/97/97951468a27f40478ac11dfac7840962/com.yek.lafaso.1511101007.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=6342&listId=2&position=57&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=f1ba812f90f78e1b8a4536747b22f95b%3BcdrInfo%3A201512011133251551995%5E3%5E11239%5E25.0%5E20709%5E5860%5E6342%5ECPA%5Eec90c908959111e488e3101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E25.0%5E30%5E1006533%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AC%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"豆瓣", "11.1M", "http://122.11.38.214/dl/appdl/application/apk/99/999d40a887cb426586e321abc1e7231e/com.douban.frodo.1511202117.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=autoList&subsource=101%3B62&listId=2&position=62&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=5c5f03c51cdddd613d7176d84867adb1%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"酒仙网", "6.7M", "http://122.11.38.214/dl/appdl/application/apk/9f/9f8a5201ad234e2daff42f34d3eef243/com.jiuxianapk.ui.1511161113.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=9988&listId=2&position=68&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=0773eb09d5a2e35009ef229b4d29d284%3BcdrInfo%3A201512011133251552000%5E3%5E15406%5E16.0%5E20498%5E9521%5E9988%5ECPA%5E34c14338959211e488e3101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E16.0%5E30%5E10059455%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AC%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"},
        {"新秦时明月", "198M", "http://122.11.38.214/dl/appdl/application/apk/59/59115ee57c374c21880e5cf44e2ace74/com.sparklykey.qinsmoon.huawei.1511261937.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=gamePositonList&listId=2&position=86&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=0773eb09d5a2e35009ef229b4d29d284%3BserviceType%3A0%3Bisshake%3A0&encryptType=1"}
    };
    
    
    public static void fillProviderData(DataProvider provider)
    {
        Random rand = new Random();
        int randIndex = 0;
        //BaseBean info = null;
        //Class<? extends AbsBasicView> clazz = null;
        for(int i = 0; i < 100; i++)
        {

            randIndex = rand.nextInt(10);
            
            if(randIndex < 5)
            {
                DownloadFileInfo info = new DownloadFileInfo("123466");
                info.downloadStatus = rand.nextInt(7);
                info.downloadProgress = rand.nextInt(101);
                provider.addData(info, ViewFactory.getViewTypeByClass(DownloadListItemView.class));
            }
            else
            {
                ProgressBean bean = new ProgressBean();
                bean.progress = rand.nextInt(101);
                if(bean.progress == 100)
                {
                    bean.secondaryProgress = 100;
                }
                else
                {
                    bean.secondaryProgress = bean.progress + rand.nextInt(101 - bean.progress);
                }
                provider.addData(bean, ViewFactory.getViewTypeByClass(ProgressView.class));
            }
        }
    }
    

    public static void fillProvider(DataProvider provider)
    {
        //DownloadFileInfo info = null;
        for(String[] data : TEST_DOWNLOAD_FILE_URL)
        {
            provider.addData(new DownloadFileInfo(data[0], data[1], data[2]), 
                    ViewFactory.getViewTypeByClass(DownloadListItemView.class));
        }
    }
}
