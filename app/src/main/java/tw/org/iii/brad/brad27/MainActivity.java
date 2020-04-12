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

    private MyAsyncTask myAsyncTask;
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

    private class MyAsyncTask extends AsyncTask<String,Object,String>{    //*1   *4一參數改為String  *9二參數改為Integer *G二再改為Object 下面也都改一改  再改三為String
        private int total, i;                                               //*A
        @Override                                               //*2 generate帶入所有的on方法
        protected void onPreExecute() {                     //執行前
            Log.v("brad","onPreExecute");
            super.onPreExecute();
            mesg.append("Start...\n");
        }

        @Override
        protected void onPostExecute(String result) {          //執行後
            Log.v("brad","onPostExecute : " + result);
            mesg.append(result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Object... values) {   //更新進度  *E 改為Integer
            Log.v("brad","onProgressUpdate" + values[0]);
            super.onProgressUpdate(values);
            mesg.append(values[1] + " -> " + values[0] + "% \n" );  //*F
        }

        @Override
        protected void onCancelled(String result) {            //取消的overload
            Log.v("brad","onCancelled " + result);
            mesg.append("onCancelled " + result + "\n");
            super.onCancelled(result);
        }

        @Override
        protected void onCancelled() {                      //取消
            Log.v("brad","onCancelled");
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... names) {      //*1 必須要實做的方法  *5改參數為String... names
            Log.v("brad","doInBackground");
            total = names.length;                               //*B
            for(String name : names){                           //*5
                i++;                                            //*C
                try {
                    Thread.sleep(3*1000);                 //*7
                }catch (Exception e){     }
                Log.v("brad",name);

//                mesg.append(name + "\n");
                publishProgress((int)Math.ceil(i*1.0/total*100), name); //*8 *D 傳參數
            }
            if(isCancelled()){
                return "Cancel";
            }else{
                return "Good Game";
            }
        }
    }

}
