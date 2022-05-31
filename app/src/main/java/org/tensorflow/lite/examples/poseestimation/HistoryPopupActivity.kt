package org.tensorflow.lite.examples.poseestimation

import android.graphics.*
import android.icu.text.AlphabeticIndex
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_resultpopup.*
import org.tensorflow.lite.examples.poseestimation.data.Device
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.ml.ModelType
import org.tensorflow.lite.examples.poseestimation.ml.MoveNet
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HistoryPopupActivity: AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var okButton: Button
    private lateinit var comment: TextView
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var commentImageView: ImageView
    private lateinit var wrongAngleDifference1: TextView
    private lateinit var wrongAngleDifference2: TextView
    private lateinit var wrongAngleDifference3: TextView
    private lateinit var wrongAngleDifference4: TextView
    private lateinit var wrongAngleDifference5: TextView
    private lateinit var feedback: TextView

    private lateinit var poseAngleDifferences: Array<List<Float?>?>

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //firebase firestore
    private lateinit var firestore: FirebaseFirestore

    var item: String? = null
    var videoPath: String? = null
    var scoreList: List<Float>? = null
    var addressAngleDifference: List<Float?>? = null
    var pushawayAngleDifference: List<Float?>? = null
    var downswingAngleDifference: List<Float?>? = null
    var backswingAngleDifference: List<Float?>? = null
    var forwardswingAngleDifference: List<Float?>? = null
    var followthroughAngleDifference: List<Float?>? = null
    var bitmapOutputList: List<String?>? = null
    var bitmapList: List<String?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historypopup)

        spinner = findViewById(R.id.history_spinner)
        okButton = findViewById(R.id.history_ok)
        comment = findViewById<TextView>(R.id.history_comment)
        commentImageView = findViewById(R.id.history_comment_image)
        wrongAngleDifference1 = findViewById<TextView>(R.id.history_wrongAngle1)
        wrongAngleDifference2 = findViewById<TextView>(R.id.history_wrongAngle2)
        wrongAngleDifference3 = findViewById<TextView>(R.id.history_wrongAngle3)
        wrongAngleDifference4 = findViewById<TextView>(R.id.history_wrongAngle4)
        wrongAngleDifference5 = findViewById<TextView>(R.id.history_wrongAngle5)
        feedback = findViewById<TextView>(R.id.history_feedback)
        imageView = findViewById(R.id.history_posture_image)
        videoView = findViewById(R.id.history_video)
//        surfaceView = findViewById(R.id.result_posture_surface)

        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.history_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter

        item = intent.getStringExtra("itemvideopath")

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

//        Log.d("TAG", "myDialog: $videoPath")

        val mc = MediaController(this@HistoryPopupActivity)
        mc.setAnchorView(videoView)
        videoView.setMediaController(mc)
        videoView.setVideoURI(Uri.fromFile(File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "${item}.mp4")))


//        var VideoList = arrayListOf<PostureListData>()
        firestore.collection("videolist")
            .whereEqualTo("uid", firebaseAuth.uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("TAG", "myDialog: $result")

                for(document in result) {

                    if (document["videoPath"].toString() == item ) {
                        videoPath = item
                        scoreList = document["scoreList"] as List<Float>?
                        Log.d("TAG", "onCreate: $scoreList")
                        addressAngleDifference = document["addressAngleDifference"] as List<Float?>?
                        pushawayAngleDifference = document["pushawayAngleDifference"] as List<Float?>?
                        downswingAngleDifference = document["downswingAngleDifference"] as List<Float?>?
                        backswingAngleDifference = document["backswingAngleDifference"] as List<Float?>?
                        forwardswingAngleDifference = document["forwardswingAngleDifference"] as List<Float?>?
                        followthroughAngleDifference = document["followthroughAngleDifference"] as List<Float?>?
                        bitmapOutputList = document["bitmapOutputList"] as List<String?>?
                        bitmapList = document["bitmapList"] as List<String?>?

                        var addressOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![0])
                        var pushawayOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![1])
                        var downswingOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![2])
                        var backswingOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![3])
                        var forwardswingOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![4])
                        var followthroughOutputBitmap = BitmapFactory.decodeFile(bitmapOutputList!![5])

                        var addressAngleDifferences = addressAngleDifference
                        var pushawayAngleDifferences = pushawayAngleDifference
                        var downswingAngleDifferences = downswingAngleDifference
                        var backswingAngleDifferences = backswingAngleDifference
                        var forwardswingAngleDifferences = forwardswingAngleDifference
                        var followthroughAngleDifferences = followthroughAngleDifference

                        poseAngleDifferences = arrayOf(addressAngleDifferences, pushawayAngleDifferences,
            downswingAngleDifferences, backswingAngleDifferences, forwardswingAngleDifferences, followthroughAngleDifferences)

                        var addressScore = scoreList!![0]
                        var pushawayScore = scoreList!![1]
                        var downswingScore = scoreList!![2]
                        var backswingScore = scoreList!![3]
                        var forwardswingScore = scoreList!![4]
                        var followthroughScore = scoreList!![5]

                        var scores = arrayOf(addressScore, pushawayScore, downswingScore, backswingScore, forwardswingScore, followthroughScore)
                        var avgScore = getAvgScore(scores)

                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    wrongAngleDifference1.text = null
                                    wrongAngleDifference2.text = null
                                    wrongAngleDifference3.text = null
                                    wrongAngleDifference4.text = null
                                    wrongAngleDifference5.text = null
                                    feedback.text = null
                                    if (getSelectedSpinnerItem() == 0) {
                                        videoView.visibility = View.VISIBLE
                                        imageView.visibility = View.INVISIBLE
                                        commentImageView.visibility = View.INVISIBLE

                                        videoView.requestFocus()
                                        videoView.start()

                                        comment.text = "평균 점수: $avgScore"

                                    } else if (getSelectedSpinnerItem() == 1) {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(addressOutputBitmap != null) {
                                            imageView.setImageBitmap(addressOutputBitmap)
                                            resultImage(addressScore)

                                        }  else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }

                                    } else if (getSelectedSpinnerItem() == 2) {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(pushawayOutputBitmap != null) {
                                            imageView.setImageBitmap(pushawayOutputBitmap)
                                        } else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }

                                    } else if (getSelectedSpinnerItem() == 3) {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(downswingOutputBitmap != null) {
                                            imageView.setImageBitmap(downswingOutputBitmap)
                                        } else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }
                                    } else if (getSelectedSpinnerItem() == 4) {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(backswingOutputBitmap != null) {
                                            imageView.setImageBitmap(backswingOutputBitmap)
                                        } else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }
                                    } else if(getSelectedSpinnerItem() == 5) {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(forwardswingOutputBitmap != null) {
                                            imageView.setImageBitmap(forwardswingOutputBitmap)
                                        } else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }
                                    } else {
                                        videoView.visibility = View.INVISIBLE
                                        imageView.visibility = View.VISIBLE
                                        commentImageView.visibility = View.VISIBLE

                                        if(followthroughOutputBitmap != null) {
                                            imageView.setImageBitmap(followthroughOutputBitmap)
                                        } else {
                                            imageView.setImageResource(R.drawable.bowling)
                                        }
                                    }

                            }
                        }

                        okButton.setOnClickListener {
                            finish()
                        }
                    }
                }


            }
            .addOnFailureListener { exception ->

            }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getAvgScore(array: Array<Float>):Float{
        var sum = 0.0f
        var count = 0
        for(i in 0 until array.size){
            if(array[i]!=0.0f) {
                sum += array[i]
                count = i + 1
            }

        }
        return sum/count
    }

    fun getSelectedSpinnerItem(): Int {
        return spinner.selectedItemPosition
    }

    fun resultImage(score: Float){
        if(score >= 40) {
            commentImageView.setImageResource(R.drawable.result_good)
        } else if(score >= 35) {
            commentImageView.setImageResource(R.drawable.result_warning)
        } else {
            commentImageView.setImageResource(R.drawable.result_bad)
        }
    }

    fun feedbackAddressAngleDiffernce(addressAngleDifferences: List<Float?>) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${addressAngleDifferences[0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${addressAngleDifferences[1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${addressAngleDifferences[2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${addressAngleDifferences[3]}"
        if (addressAngleDifferences[0]!! >= 10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 벌어졌네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }else if (addressAngleDifferences[0]!! <= -10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 좁네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }

        if (addressAngleDifferences[1]!! >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(addressAngleDifferences[1]!! <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (addressAngleDifferences[2]!! >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(addressAngleDifferences[2]!! <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }
        if (addressAngleDifferences[3]!! >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(addressAngleDifferences[3]!! <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }
        if(feedback.text == null){
            feedback.text = "짝짝짝! 완벽한 자세에요!"
        }
    }

    fun feedbackPushAngleDiffernce(pushawayAngleDifferences: FloatArray) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${pushawayAngleDifferences!![0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${pushawayAngleDifferences!![1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${pushawayAngleDifferences!![2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${pushawayAngleDifferences!![3]}"
        wrongAngleDifference5.text = "왼쪽 무릎 각도 차이: ${pushawayAngleDifferences!![4]}"

        if (pushawayAngleDifferences!![0] >= 10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 벌어졌네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }else if (pushawayAngleDifferences!![0] <= -10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 좁네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }

        if (pushawayAngleDifferences!![1] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(pushawayAngleDifferences[1] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (pushawayAngleDifferences!![2] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(pushawayAngleDifferences[2] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (pushawayAngleDifferences!![3] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(pushawayAngleDifferences[3] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (pushawayAngleDifferences!![4] >= 10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(pushawayAngleDifferences[4] <= -10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if(feedback.text == null){
            feedback.text = "짝짝짝! 완벽한 자세에요!"
        }
    }

    fun feedbackDownAngleDiffernce(angleDifferences: FloatArray) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${angleDifferences!![0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${angleDifferences!![1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${angleDifferences!![2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${angleDifferences!![3]}"
        wrongAngleDifference5.text = "왼쪽 무릎 각도 차이: ${angleDifferences!![4]}"

        if (angleDifferences!![0] >= 10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 벌어졌네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }else if (angleDifferences!![0] <= -10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 좁네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }

        if (angleDifferences!![1] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[1] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![2] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[2] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![3] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[3] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![4] >= 10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[4] <= -10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if(feedback.text == null){
            feedback.text = "짝짝짝! 완벽한 자세에요!"
        }
    }

    fun feedbackBackAngleDiffernce(angleDifferences: FloatArray) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${angleDifferences!![0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${angleDifferences!![1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${angleDifferences!![2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${angleDifferences!![3]}"
        wrongAngleDifference5.text = "왼쪽 무릎 각도 차이: ${angleDifferences!![4]}"

        if (angleDifferences!![0] >= 10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 벌어졌네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }else if (angleDifferences!![0] <= -10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 좁네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }

        if (angleDifferences!![1] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[1] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![2] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[2] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![3] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[3] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![4] >= 10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[4] <= -10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }
    }

    fun feedbackForwardAngleDiffernce(angleDifferences: FloatArray) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${angleDifferences!![0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${angleDifferences!![1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${angleDifferences!![2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${angleDifferences!![3]}"
        wrongAngleDifference5.text = "왼쪽 무릎 각도 차이: ${angleDifferences!![4]}"

        if (angleDifferences!![0] >= 10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 벌어졌네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }else if (angleDifferences!![0] <= -10.0) {
            feedback.text = "오른쪽 팔꿈치가 많이 좁네요! 팔을 90도로 만들어서 팔꿈치를 옆구리에 딱 붙여주세요!\n"
        }

        if (angleDifferences!![1] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[1] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 어깨 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![2] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[2] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 골반 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![3] >= 10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[3] <= -10.0) {
            feedback.text = "${feedback.text}오른쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }

        if (angleDifferences!![4] >= 10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 벌어졌네요! 각도를 조금 줄여주세요!\n"
        }else if(angleDifferences[4] <= -10.0) {
            feedback.text = "${feedback.text}왼쪽 무릎 각도가 많이 좁네요! 각도를 조금 벌려주세요!\n"
        }
    }

    fun feedbackFollowAngleDiffernce(angleDifferences: FloatArray) {
        wrongAngleDifference1.text = "오른쪽 팔꿈치 각도 차이: ${angleDifferences!![0]}"
        wrongAngleDifference2.text = "오른쪽 어깨 각도 차이: ${angleDifferences!![1]}"
        wrongAngleDifference3.text = "오른쪽 골반 각도 차이: ${angleDifferences!![2]}"
        wrongAngleDifference4.text = "오른쪽 무릎 각도 차이: ${angleDifferences!![3]}"
        wrongAngleDifference5.text = "왼쪽 무릎 각도 차이: ${angleDifferences!![4]}"
        if (angleDifferences!![0] >= 10.0 || angleDifferences[0] <= -10.0) {
            feedback.text = "팔로스루 자세에서 오른쪽 팔꿈치가 많이 벌어졌어요.. 이러면 공이 뭐 어떻게 되요\n"
        }
        if (angleDifferences!![1] >= 10.0 || angleDifferences[1] <= -10.0) {
            feedback.text = "${feedback.text}팔로스로 오른쪽 어깨 각도가 잘못됐당\n"
        }
        if (angleDifferences!![2] >= 10.0 || angleDifferences[2] <= -10.0) {
            feedback.text = "${feedback.text}팔로스로 오른쪽 골반 각도가 잘못됐당\n"
        }
        if (angleDifferences!![3] >= 10.0 || angleDifferences[3] <= -10.0) {
            feedback.text = "${feedback.text}팔로스로 오른쪽 어깨 각도가 잘못됐당\n"
        }
        if (angleDifferences!![4] >= 10.0 || angleDifferences[4] <= -10.0) {
            feedback.text = "${feedback.text}팔로스로 왼쪽 무릎 각도가 잘못됐당\n"
        }
        if(feedback.text == null){
            feedback.text = "짝짝짝! 완벽한 자세에요!"
        }
    }

}