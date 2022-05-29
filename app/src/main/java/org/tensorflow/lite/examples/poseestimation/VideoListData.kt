package org.tensorflow.lite.examples.poseestimation

class VideoListData(
    var uid : String? = null,
    var date : Long? = System.currentTimeMillis(),
    var videoID : String? = "미설정",
    var feedback : String? = "피드백",
    var score : Int? = 0,
    val isFavorite : Boolean? = false
) {}