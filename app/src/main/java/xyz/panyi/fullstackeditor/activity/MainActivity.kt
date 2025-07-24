package xyz.panyi.fullstackeditor.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import xyz.panyi.fullstackeditor.R
import xyz.panyi.fullstackeditor.util.Log

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }
    
    private fun initView(){
        findViewById<View>(R.id.btn_select_video).setOnClickListener{
            Log.i(TAG, "select video button clicked.")
            Log.e(TAG,"select video button clicked end.")
        }
    }
}//end class