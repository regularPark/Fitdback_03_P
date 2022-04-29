package com.fitdback.algorithm

import android.content.Context
import android.graphics.PointF
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.fitdback.posedetection.R
import kotlin.math.acos
import kotlin.math.sqrt

class FeedbackAlgorithm {


    // 스쿼트 먼저
    companion object {
        var exr_mode: String = "Empty"

        var hka_l_angle: Double = 0.0 //왼쪽 엉덩이, 무릎, 발 각도
        var hka_r_angle: Double = 0.0 //오른쪽 엉덩이, 무릎, 발 각도

        var total_exr_time: Long = 0
        var exr_time: Long = 0

        var isSquat: Boolean = false //스쿼트 동작 완료
        var isStand: Boolean = false //서있는지 판단
        var isPlaying: Boolean = false
        var cnt_tf: Boolean = false //성공 횟수 추가 판단
        var exr_cnt: Int = 0 //동작 완료 횟수
        val pi: Double = 3.141592
        /*val soundPool = SoundPool.Builder().build()
        var soundId: Int = 0*/


        private fun cal_dist(p1: PointF, p2: PointF): Double {
            return sqrt((((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y))).toDouble())
        }

        private fun cal_angle(p1: PointF, p2: PointF, p3: PointF): Double {

            val dist1 = cal_dist(p1, p2)
            val dist2 = cal_dist(p2, p3)
            val dist3 = cal_dist(p3, p1)

            return acos((((dist1 * dist1) + (dist2 * dist2) - (dist3 * dist3)) / (2 * dist1 * dist2))) * 180 / pi
        }

        //DrawView 164줄에서 squat 함수 호출
        fun squat(context: Context, mDrawPoint: ArrayList<PointF>) {
            //total_exr_time = System.currentTimeMillis() - exr_time // 운동시간
            hka_l_angle = cal_angle(mDrawPoint[8], mDrawPoint[9], mDrawPoint[10])
            hka_r_angle = cal_angle(mDrawPoint[11], mDrawPoint[12], mDrawPoint[13])
            //soundId = MediaPlayer.load(context, R.raw.sound1, 1)

            if ((170.toDouble() <= hka_l_angle && hka_l_angle <= 180.toDouble()) && (170.toDouble() <= hka_r_angle && hka_r_angle <= 180.toDouble())) {
                isStand = true
                //Log.d("zxcv", "stand complete")

                //운동 횟수 추가 판단되면 스쿼트 동작 완료 후 기본자세(stand)로 돌아가면 횟수 추가
                if (cnt_tf) {
                    cnt_tf = false
                    exr_cnt++

                    println("Exercising : hkal = " + hka_l_angle + ", hkar = " + hka_r_angle + " cnt = " + exr_cnt)
                    //soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

                    /*val mediaPlayer = MediaPlayer.create(this, R.raw.sound1)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener { mediaPlayer.release()}*/

                    Toast.makeText(context, "운동 성공~!", Toast.LENGTH_SHORT).show()
                    //Log.d("asdf", "squat complete")
                }
            } else if ((100.toDouble() >= hka_l_angle) && (100.toDouble() >= hka_r_angle) && isPlaying) {
                //스쿼트 자세로 판단되면 플래그들을 모두 바꿔줌
                isStand = false
                isSquat = true
                cnt_tf = true

            }
        }


    }
}