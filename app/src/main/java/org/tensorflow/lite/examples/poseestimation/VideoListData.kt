package org.tensorflow.lite.examples.poseestimation

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import org.tensorflow.lite.examples.poseestimation.data.Person
import java.util.*

data class VideoListData(
//    var createdAt
    @ServerTimestamp
    var createdAt: Date? = null,
    var uid : String? = null,
//    var date : Long? = System.currentTimeMillis(),
    var videoPath : String? = "미설정",
//    var feedback : String? = "피드백",
    var score : Float? = 0.0f,
    var scoreList: List<Float>? = null,
    var addressAngleDifference: List<Float?>? = null,
    var pushawayAngleDifference: List<Float?>? = null,
    var downswingAngleDifference: List<Float?>? = null,
    var backswingAngleDifference: List<Float?>? = null,
    var forwardswingAngleDifference: List<Float?>? = null,
    var followthroughAngleDifference: List<Float?>? = null,
    var bitmapOutputList: List<String?>? = null,
    var bitmapList: List<String?>? = null,
    val isFavorite : Boolean = false
)