package org.tensorflow.lite.examples.poseestimation

data class PostureListData(
    var uid : String,
    var videoID : String,
    var image : String,
    var date : String,
    var score : String,
    var isFavorite : Boolean,
    var feedback : String
)
