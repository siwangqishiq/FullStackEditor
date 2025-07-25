package xyz.panyi.fullstackeditor.vm

import android.content.Context
import android.provider.MediaStore
import xyz.panyi.fullstackeditor.data.PickerFile
import xyz.panyi.fullstackeditor.util.Log


object MediaQuery {
    const val TAG = "MediaQuery"
    
    fun queryVideoFileData(context: Context) : List<PickerFile> {
        Log.i(TAG,"query video file start")
        
        val result = ArrayList<PickerFile>(16)
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.BUCKET_ID
        )
        
        val cur = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, MediaStore.Video.Media.DATE_MODIFIED + " DESC"
        )
        
        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast) {
                    val item = PickerFile().apply {
                        name = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                        path = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        duration = cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        width = cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
                        height = cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
                        size = cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    }
                    result.add(item)
                    cur.moveToNext()
                }// end while
            }
            cur.close()
        }
        Log.i(TAG,"query video file end")
        return result
    }
}