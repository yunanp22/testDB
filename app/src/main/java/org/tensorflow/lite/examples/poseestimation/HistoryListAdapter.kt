package org.tensorflow.lite.examples.poseestimation

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.example_dialog.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.history_list_item.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryListAdapter(val context: Context, val VideoList: ArrayList<VideoListData>) : BaseAdapter() {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //firebase firestore
    private lateinit var firestore: FirebaseFirestore

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */

        val view : View
        val holder : ViewHolder
        //파이어베이스 로드
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.history_list_item, null)
            holder = ViewHolder()
            holder.view_image1 = view.findViewById(R.id.posturePhotoImg)
            holder.view_text1 = view.findViewById(R.id.posture_number)
//            holder.view_text2 = view.findViewById(R.id.date)
//            holder.view_text3 = view.findViewById(R.id.correct_score)
            holder.view_favorite = view.findViewById(R.id.checkbox_favorite)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = VideoList[position]
//        val resourceId = context.resources.getIdentifier(item.image, "drawable", context.packageName)

        holder.view_image1?.setImageResource(R.drawable.bowling)
        holder.view_text1?.text = item.videoPath
//        holder.view_text2?.text = item.score
//        holder.view_text3?.text = item.videoID
        holder.view_favorite?.isChecked = item.isFavorite!!

        // 스위치 상태가 변할 때마다 이 여부를 해당 position의 아이템에 스위치 상태를 설정
        holder.view_favorite?.setOnCheckedChangeListener { buttonView, isChecked ->
            holder.view_favorite?.isChecked = isChecked
        }

        // 하트 즐겨찾기 체크 박스 클릭시
        view.checkbox_favorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(view.checkbox_favorite.isChecked){
                    firestore.collection("videolist")
                        .whereEqualTo("uid", firebaseAuth.uid)
                        .get()
                        .addOnSuccessListener{ result ->
                            //videoID를 통해 구분해서 isFavorite 값 업데이트
                            for(document in result){
                                if(document["videoPath"].toString() == item.videoPath ){
                                    if(document["favorite"] as Boolean){
                                        var docName : String = document.id
                                        firestore.collection("videolist").document(docName).update("favorite",false)
                                    }else {
                                        var docName : String = document.id
                                        firestore.collection("videolist").document(docName).update("favorite",true)
                                    }
                                }
                            }
                        }
                } else {
                    firestore.collection("videolist")
                        .whereEqualTo("uid", firebaseAuth.uid)
                        .get()
                        .addOnSuccessListener{ result ->
                            //videoID를 통해 구분해서 isFavorite 값 업데이트
                            for(document in result){
                                if(document["videoPath"].toString() == item.videoPath ){
                                    if(document["favorite"] as Boolean){
                                        var docName : String = document.id
                                        firestore.collection("videolist").document(docName).update("favorite",false)
                                    }else {
                                        var docName : String = document.id
                                        firestore.collection("videolist").document(docName).update("favorite",true)
                                    }
                                }
                            }
                        }
                }
            }
        })


        // 연습목록 리스트 클릭시
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, HistoryPopupActivity::class.java)
                intent.putExtra("itemvideopath", item.videoPath)

                context.startActivity(intent)
            }
        })

        return view
    }

    fun log(){
        Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
    }

    override fun getItem(p0: Int): Any {
        return VideoList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return VideoList.size
    }

    private class ViewHolder {
        var view_image1 : ImageView? = null
        var view_text1 : TextView? = null
        var view_text2 : TextView? = null
        var view_text3 : TextView? = null
        var view_favorite : CheckBox? = null
    }
}

class CustomDialog(view: View, context: Context) {
    private val dialog = Dialog(context)
    private val view = view

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //firebase firestore
    private lateinit var firestore: FirebaseFirestore

    val c = context

    fun myDialog(videoPath : String?){
        dialog.setContentView(R.layout.example_dialog)

//        var path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)
//        dialog.dialog_video.setVideoPath(""+path + "/VID_2022_05_27_22_49_27_292.mp4")
        //문서 업데이트
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var uri: String? = null
        Log.d("TAG", "myDialog: $videoPath")

//        var VideoList = arrayListOf<PostureListData>()
        firestore.collection("videolist")
            .whereEqualTo("uid", firebaseAuth.uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("TAG", "myDialog: $result")
                for(document in result) {


                    if (document["videoPath"].toString() == videoPath ) {
                        uri = videoPath
                        Log.d("TAG", "myDialog: $uri")

//                        dialog.dialog_video.setVideoURI(Uri.fromFile(File(
//                            Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_DCIM), "${uri}.mp4")))
//
//                        dialog.dialog_video.setOnPreparedListener {
//                            val mediaController = MediaController(c)
//                            mediaController.setAnchorView(dialog.dialog_video)
//                            dialog.dialog_video.setMediaController(mediaController)
//                            /*
//                            m : MediaPlayer ->
//                                m.setOnVideoSizeChangedListener { m : MediaPlayer?, width: Int, height: Int ->
//
//                                }
//
//                             */
//
//                        }
//                        dialog.dialog_video.requestFocus()
//                        dialog.dialog_video.start()
                        val videoView: VideoView = dialog.findViewById(R.id.dialog_video)
                        val mc = MediaController(dialog.context)
//                val path = "android.resource://" + activity?.packageName + "/" + video
                        videoView.setVideoURI(Uri.fromFile(File(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM), "${uri}.mp4")))
                        mc.setAnchorView(videoView)
                        videoView.setMediaController(mc)
                        videoView.requestFocus()
                        videoView.start()
                    }
                }




//                for(document in result){
//
////                    // 평균 점수 불러오기
////                    var a = document["score"].toString()
////                    var c : Int = a.toInt()
////                    sum += c
////                    count++
////                    recentScore = c
////
////                    // 기록 불러오기
////                    val date = document["date"]
////                    val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
////                    val strDate = dateFormat.format(date).toString()
////                    VideoList.add(PostureListData(
////                        document["uid"].toString(),
////                        document["videoID"].toString(),
////                        "bowling",
////                        strDate,
////                        "정확도 "+ document["score"] +"%",
////                        document["favorite"] as Boolean,
////                        document["feedback"].toString()
////                    )
////                    )
//                }
//                var avg = 0
//                //평균 계산 후 avg에 업데이트
//                if (count == 0){
//                    avg = 0
//                } else {
//                    avg = sum/count
//                }
//
//                var userInfo = UsersData()
//                //평균으로 등급 부여
//                var grade : Int = when(avg){
//                    in 95 until 100 -> 1
//                    in 85 until 94 -> 2
//                    in 84 until 84 -> 3
//                    in 65 until 74 -> 4
//                    in 55 until 64 -> 5
//                    in 45 until 54 -> 6
//                    in 35 until 44 -> 7
//                    in 25 until 34 -> 8
//                    else -> 9
//                }
//                println(grade)
//                userInfo.avg = avg
//                userInfo.grade = grade
//                userInfo.recentScore = recentScore
//                firestore?.collection("users")?.document(firebaseAuth?.uid.toString())?.update("avg", userInfo.avg,"grade", userInfo.grade, "recentScore", userInfo.recentScore )



            }
            .addOnFailureListener { exception ->

            }

        Log.d("TAG", "myDialog11: $uri")
//        dialog.dialog_video.setVideoURI(Uri.fromFile(File(
//            Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "${uri}.mp4")))
//
//        dialog.dialog_video.setOnPreparedListener {
//            val mediaController = MediaController(c)
//            mediaController.setAnchorView(dialog.dialog_video)
//            dialog.dialog_video.setMediaController(mediaController)
//            /*
//            m : MediaPlayer ->
//                m.setOnVideoSizeChangedListener { m : MediaPlayer?, width: Int, height: Int ->
//
//                }
//
//             */
//
//        }
//        dialog.dialog_video.requestFocus()
//        dialog.dialog_video.start()


        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
//        dialog.dialog_text.setText(posture)


        dialog.show()
        dialog.dialog_button.setOnClickListener{
            dialog.dismiss()
        }
    }

}