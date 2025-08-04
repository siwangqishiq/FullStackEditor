package xyz.panyi.fullstackeditor.activity

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.panyi.fullstackeditor.R



class VideoEditorActivity : AppCompatActivity(){
    private  lateinit var  videoPlayer: ExoPlayer
    private lateinit var textureView: TextureView
    private lateinit var frameView: ImageView
    private lateinit var timelineRecycler: RecyclerView
    private lateinit var timelineIndicator: View
    private lateinit var mediaSource: MediaSource

    // 视频URI
    private var videoUri: Uri? = null
    // 视频帧缩略图列表
    private val thumbnails = mutableListOf<Bitmap>()
    // 当前播放位置
    private var currentPosition = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_editor)

        // 初始化视图
        textureView = findViewById(R.id.video_texture_view)
        frameView = findViewById(R.id.video_frame_view)
        timelineRecycler = findViewById(R.id.timeline_recycler)
        timelineIndicator = findViewById(R.id.timeline_indicator)

        // 获取视频URI
        videoUri = intent.data
        if (videoUri == null) {
            finish()
            return
        }

        // 初始化播放器
        initPlayer()
        // 初始化时间轴
        initTimeline()
        // 初始化功能菜单
        initFunctionMenu()

    }

    @OptIn(UnstableApi::class) private fun initPlayer(){
        // 新版本初始化
        val player = ExoPlayer.Builder(this)
            .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            .build()

        // 绑定视图（使用新的PlayerView）
        val playerView = PlayerView(this)
        playerView.player = player
    }

    private fun  initTimeline(){
        timelineRecycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        // 生成视频缩略图
        generateThumbnails()

        //设置适配器
        val adapter = TimelineAdapter(thumbnails)
        adapter.onScrollListener =  {
            position ->
            val timeMs = position * 1000L // Assuming each thumbnail represents 1 second
            updateFramePreview(timeMs)
        }
        timelineRecycler.adapter =  adapter

        // 添加滚动监听
        timelineRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val centerX = recyclerView.width / 2
                val centerPosition = recyclerView.findChildViewUnder(centerX.toFloat(), 0f)?.let {
                    recyclerView.getChildAdapterPosition(
                        it
                    )
                } ?: 0
                updateFramePreview(centerPosition * 1000L)
            }
        })
    }


    private  fun  generateThumbnails(){
        // 使用MediaMetadataRetriever获取视频帧
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(this, videoUri)

        val  duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        // 每1秒取一帧
        for(i in 0 until duration/1000){
             val  timeUs = i* 1000 * 1000L
             val  bitmap  = retriever.getFrameAtTime(timeUs,
                 MediaMetadataRetriever.OPTION_CLOSEST)
            bitmap?.let { thumbnails.add(it) }
        }

        retriever.release()

    }

    private  fun  updateFramePreview(positionMs: Long){
         currentPosition = positionMs
         //显示当前帧
         if (videoPlayer.isPlaying) {
             videoPlayer.pause()
             textureView.visibility = View.GONE
             frameView.visibility = View.VISIBLE

             // 获取当前帧
             val retriever = MediaMetadataRetriever()
             retriever.setDataSource(this, videoUri)
             val bitmap = retriever.getFrameAtTime(
                 positionMs * 1000,
                 MediaMetadataRetriever.OPTION_CLOSEST)
             retriever.release()

             bitmap?.let {
                 frameView.setImageBitmap(it)
             }
         } else {
             videoPlayer.seekTo(positionMs)
         }
    }

    private fun initFunctionMenu(){
        val menuContainer = findViewById<LinearLayout>(R.id.function_menu_container)

        // 添加功能按钮
        val functions = listOf("剪辑", "音频", "文本", "滤镜", "特效", "比例")

        functions.forEach { text ->
            val button = Button(this).apply {
                this.text = text
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = 16.dpToPx()
                }
                setOnClickListener {
                    when (text) {
                        "剪辑" -> showClipMenu()
                        "音频" -> showAudioMenu()
                        // 其他功能...
                    }
                }
            }
            menuContainer.addView(button)
        }
    }

    private  fun  showClipMenu(){

    }

    private  fun  showAudioMenu(){

    }

    // 时间轴适配器
    inner class TimelineAdapter(private val thumbnails: List<Bitmap>) :
        RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

        var onScrollListener: ((Int) -> Unit)? = null

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.findViewById(R.id.thumbnail_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline_thumbnail, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageBitmap(thumbnails[position])
            // Optionally, you can add click listener here if you want
            holder.itemView.setOnClickListener {
                onScrollListener?.invoke(position)
            }
        }

        override fun getItemCount(): Int = thumbnails.size
    }

    // dp转px扩展函数
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.release()
    }

}