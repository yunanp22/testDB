package org.tensorflow.lite.examples.poseestimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.custom_dialog_age.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.history_list_item.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //firebase firestore
    private lateinit var firestore: FirebaseFirestore


    companion object {
        fun newInstance() : HistoryFragment {
            return HistoryFragment()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결해주는 파트
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        //파이어베이스 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var sum : Int = 0
        var count : Int = 0
        var recentScore : Int = 0

        var VideoList = arrayListOf<PostureListData>()
        firestore.collection("videoList")
            .whereEqualTo("uid", firebaseAuth.uid)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){

                    // 평균 점수 불러오기
                    var a = document["score"].toString()
                    var c : Int = a.toInt()
                    sum += c
                    count++
                    recentScore = c

                    // 기록 불러오기
                    val date = document["date"]
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("ko", "KR"))
                    val strDate = dateFormat.format(date).toString()
                    VideoList.add(PostureListData(
                        document["uid"].toString(),
                        document["videoID"].toString(),
                        "bowling",
                        strDate,
                        "정확도 "+ document["score"] +"%",
                        document["favorite"] as Boolean,
                        document["feedback"].toString()
                    )
                    )
                }
                var avg = 0
                //평균 계산 후 avg에 업데이트
                if (count == 0){
                    avg = 0
                } else {
                    avg = sum/count
                }

                var userInfo = UsersData()
                //평균으로 등급 부여
                var grade : Int = when(avg){
                    in 95 until 100 -> 1
                    in 85 until 94 -> 2
                    in 84 until 84 -> 3
                    in 65 until 74 -> 4
                    in 55 until 64 -> 5
                    in 45 until 54 -> 6
                    in 35 until 44 -> 7
                    in 25 until 34 -> 8
                    else -> 9
                }
                println(grade)
                userInfo.avg = avg
                userInfo.grade = grade
                userInfo.recentScore = recentScore
                firestore?.collection("users")?.document(firebaseAuth?.uid.toString())?.update("avg", userInfo.avg,"grade", userInfo.grade, "recentScore", userInfo.recentScore )


                //Adapter
                val list_adapter = HistoryListAdapter(requireContext(), VideoList)
                view.listview_history.adapter = list_adapter

            }
            .addOnFailureListener { exception ->

            }

        //    var uid : String,
        //    var videoID : String,
        //    var image : String,
        //    var date : String,
        //    var score : String,
        //    var isFavorite : Boolean,
        //    var feddback : String
/*
        val list_array = arrayListOf<PostureListData>(
            PostureListData("bowling", "자세1", "2022년 4월 22일", "정확도 91%"),
            PostureListData("bowling", "자세2", "2022년 4월 23일", "정확도 77%"),
            PostureListData("bowling", "자세3", "2022년 4월 24일", "정확도 80%"),
            PostureListData("bowling", "자세4", "2022년 4월 24일", "정확도 88%"),
            PostureListData("bowling", "자세5", "2022년 4월 27일", "정확도 90%"),
            PostureListData("bowling", "자세6", "2022년 4월 28일", "정확도 97%"),
            PostureListData("bowling", "자세7", "2022년 5월 1일", "정확도 33%"),
            PostureListData("bowling", "자세8", "2022년 5월 5일", "정확도 71%")
        )
*/


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 탭 이벤트
        view.tab.addOnTabSelectedListener( object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?){
                when(tab!!.position){
                    0 -> {
                        //파이어베이스 초기화
                        firebaseAuth = FirebaseAuth.getInstance()
                        firestore = FirebaseFirestore.getInstance()

                        var sum : Int = 0
                        var count : Int = 0
                        var recentScore : Int = 0

                        var VideoList = arrayListOf<PostureListData>()
                        firestore.collection("videoList")
                            .whereEqualTo("uid", firebaseAuth.uid).orderBy("date")
                            .get()
                            .addOnSuccessListener { result ->
                                for(document in result){

                                    // 평균 점수 불러오기
                                    var a = document["score"].toString()
                                    var c : Int = a.toInt()
                                    sum += c
                                    count++
                                    recentScore = c

                                    // 기록 불러오기
                                    val date = document["date"]
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("ko", "KR"))
                                    val strDate = dateFormat.format(date).toString()
                                    VideoList.add(PostureListData(
                                        document["uid"].toString(),
                                        document["videoID"].toString(),
                                        "bowling",
                                        strDate,
                                        "정확도 "+ document["score"] +"%",
                                        document["favorite"] as Boolean,
                                        document["feedback"].toString()
                                    )
                                    )
                                }
                                //평균 계산 후 avg에 업데이트
                                var avg = 0
                                if (count == 0){
                                    avg = 0
                                } else {
                                    avg = sum/count
                                }

                                var userInfo = UsersData()
                                //평균으로 등급 부여
                                var grade : Int = when(avg){
                                    in 95 until 100 -> 1
                                    in 85 until 94 -> 2
                                    in 84 until 84 -> 3
                                    in 65 until 74 -> 4
                                    in 55 until 64 -> 5
                                    in 45 until 54 -> 6
                                    in 35 until 44 -> 7
                                    in 25 until 34 -> 8
                                    else -> 9
                                }
                                println(grade)
                                userInfo.avg = avg
                                userInfo.grade = grade
                                userInfo.recentScore = recentScore
                                firestore?.collection("users")?.document(firebaseAuth?.uid.toString())?.update("avg", userInfo.avg,"grade", userInfo.grade, "recentScore", userInfo.recentScore )


                                //Adapter
                                val list_adapter = HistoryListAdapter(requireContext(), VideoList)
                                view.listview_history.adapter = list_adapter

                            }
                            .addOnFailureListener { exception ->

                            }

                        Toast.makeText(context, "잘했어", Toast.LENGTH_SHORT).show()
                    }

                    1 -> {
                        //파이어베이스 초기화
                        firebaseAuth = FirebaseAuth.getInstance()
                        firestore = FirebaseFirestore.getInstance()


                        var VideoList = arrayListOf<PostureListData>()
                        firestore.collection("videoList")
                            .whereEqualTo("uid", firebaseAuth.uid).orderBy("date")
                            .get()
                            .addOnSuccessListener { result ->
                                for(document in result){

                                    //선택된것만
                                    if(document["favorite"] == true){
                                        // 기록 불러오기
                                        val date = document["date"]
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("ko", "KR"))
                                        val strDate = dateFormat.format(date).toString()
                                        VideoList.add(PostureListData(
                                            document["uid"].toString(),
                                            document["videoID"].toString(),
                                            "bowling",
                                            strDate,
                                            "정확도 "+ document["score"] +"%",
                                            document["favorite"] as Boolean,
                                            document["feedback"].toString()
                                        )
                                        )
                                    }
                                }

                                //Adapter
                                val list_adapter = HistoryListAdapter(requireContext(), VideoList)
                                view.listview_history.adapter = list_adapter

                            }
                            .addOnFailureListener { exception ->

                            }
                        Toast.makeText(context, "no", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }
}