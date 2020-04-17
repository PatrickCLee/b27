package tw.org.iii.brad.brad27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mesg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg = findViewById(R.id.mesg);                         //*6
    }

    private MyAsyncTask myAsyncTask;                            //*18 宣告拉出來讓atest2也可用
    public void atest1(View view) {
        mesg.setText("");
        myAsyncTask = new MyAsyncTask();                        //*3
        myAsyncTask.execute("meow","John~Wick","supbro");       //*5之後改為傳入參數,觀察Log
    }

    public void atest2(View view) {
        if(myAsyncTask != null){
            Log.v("brad","status : " + myAsyncTask.getStatus().name());
            if(!myAsyncTask.isCancelled()){
                myAsyncTask.cancel(true);
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<String,Object,String>{    //*1   *4一參數改為String  *9二參數改為Integer *16二再改為Object 下面也都改一改  *17再改三為String
        private int total, i;                                               //*10
        @Override                                               //*2 generate帶入所有的on方法 並log
        protected void onPreExecute() {                     //執行前      如果要連資料庫...等前置作業可在此做
            Log.v("brad","onPreExecute");
            super.onPreExecute();
            mesg.append("Start...\n");
        }

        @Override
        protected void onPostExecute(String result) {          //執行後         *17 參數型別
            Log.v("brad","onPostExecute : " + result);
            mesg.append(result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Object... values) {   //更新進度  *14 改為Integer *16
            Log.v("brad","onProgressUpdate: " + values[0]);
            super.onProgressUpdate(values);
            mesg.append(values[1] + " -> " + values[0] + "% \n" );  //*15   *16 加傳參數 (values[0]是百分比 values[1]是names)
        }

        @Override
        protected void onCancelled(String result) {            //取消的overload    *17 參數型別
            Log.v("brad","onCancelled " + result);                      //*18
            mesg.append("onCancelled " + result );
            super.onCancelled(result);
        }

        @Override
        protected void onCancelled() {                      //取消
            Log.v("brad","onCancelled");
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... names) {      //*1 必須要實做的方法  *4改參數為String... names    *17回傳型別
            Log.v("brad","doInBackground");
            total = names.length;                                           //*11
            for(String name : names){                           //*4
                i++;                                                                //*12
                try {
                    Thread.sleep(3*1000);                       //*7
                }catch (Exception e){}
                Log.v("brad",name);                         //*4

//                mesg.append(name + "\n");                      //*6  這個方法必須在main Thread(UI Thread)執行,此處是分開的Thread
                publishProgress((int)Math.ceil(i*1.0/total*100), name); //*8 執行此方法會觸發onProgressUpdate  *13 傳int參數   *16 加傳name
            }
            if(isCancelled()){
                return "Cancel";
            }else{
                return "Good Game";                             //*17
            }
        }
    }

}
