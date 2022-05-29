package org.tensorflow.lite.examples.poseestimation

data class UsersData (
    var uid : String? = null,
    var userNickName : String? = "미설정",
    var userID : String? = null,
    var age : Int? = 0,
    var ballsize : Int? = 0,
    var grade : Int? = 9,
    var avg : Int? = 0,
    var recentScore : Int? = 0
){ }