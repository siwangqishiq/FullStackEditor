package xyz.panyi.fullstackeditor.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.panyi.fullstackeditor.R
import xyz.panyi.fullstackeditor.data.FilePickerParams
import xyz.panyi.fullstackeditor.util.Log

/**
 * 媒体文件选择
 */
class FilePickerActivity : AppCompatActivity() {
    companion object{
        const val TAG = "FilePickerActivity"
        
        const val INTENT_PARAMS = "_params"
        
        fun start(ctx: Activity, type:Int, requestCode : Int){
            ctx.startActivityForResult( Intent(ctx, FilePickerActivity::class.java).apply {
                putExtra(INTENT_PARAMS, FilePickerParams(type))
            },requestCode)
        }
    }
    
    private var params:FilePickerParams? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)
        initView()
        readParams()
    }
    
    private fun initView(){
        setSupportActionBar(findViewById(R.id.toolbar))
        setTitle(R.string.picker_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    
    private fun readParams(){
        params = intent.getSerializableExtra(INTENT_PARAMS) as FilePickerParams
        Log.i(TAG, "pick file type : ${params?.type}")
    }
}//end class