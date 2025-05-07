package space.webkombinat.epdc.Model.Controller

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.get
import org.jetbrains.annotations.Async
import java.lang.Thread.sleep

class CanvasManager(
    val screenWidth: Int,
    val screenHeight: Int,
    val screenWidthDp: Int,
    val screenHeightDp: Int
) {
    val EPD_W = 296 // EPDの画面サイズ将来的に違う画面サイズに変換できるようにする必要があるかも
    val EPD_H = 128 // pixelの数
    val width_x = (screenWidth / EPD_W).toInt()
    val virual_EPD_W: Int = EPD_W * width_x
    val virual_EPD_H: Int = EPD_H * width_x

    fun printScreenSize() {
        println("screen width px = ${screenWidth}, screen height px = ${screenHeight}")
        println("screen width dp = ${screenWidthDp}, screen height dp = ${screenHeightDp}")
        println("virtual_EPD_W (px) = ${virual_EPD_W}, virtual_EPD_H (px) = ${virual_EPD_H}")
    }

    fun bitmapToHexList(bitmap: Bitmap): MutableList<Int> {
        val bitmap_buf = bitmap
        val array_01 = mutableListOf<Int>()
        var x = 0
        var y = 0

        while (x < EPD_W) {
            while (y < EPD_H) {
                val OneOrZero = blackOrWhite(x = x, y = y, width_x = width_x, bitmap_buf = bitmap_buf)
                array_01.add(OneOrZero)
                y++
            }
            y = 0
            x++
        }
//        println("01の配列の個数 ${array_01.size}")
        Log.d("array", "${array_01}")
        return array_01
    }

    fun redZeroToOne(list: List<Int>): List<Int> {
        val array = mutableListOf<Int>()
        list.forEach { n ->
            if(n == 1) {
                array.add(0)
            } else {
                array.add(1)
            }
        }

        return array
    }

    fun listIntToByteArrayDirect(binaryList: List<Int>): ByteArray {
        val result = mutableListOf<Byte>()
        for (i in binaryList.indices step 8) {
            val subList = binaryList.subList(i, minOf(binaryList.size, i + 8))
            // subList をビット列の文字列に変換
            val byteString = subList.joinToString("")
            // 文字列が空でない場合に変換
            if (byteString.isNotEmpty()) {
                // 必要に応じて左側を0でパディング (8ビットに満たない場合)
                val paddedByteString = byteString.padStart(8, '0')
                val byteValue = paddedByteString.toInt(2).toByte()
                result.add(byteValue)
            }
        }
        return result.toByteArray()
    }

//    fun convertBinaryListToHex(binaryList: MutableList<Int>): MutableList<String> {
//        val result = mutableListOf<String>()
//        val chunkSize = 8
//
//        for (i in 0 until binaryList.size step chunkSize) {
//            val chunk = binaryList.subList(i, minOf(binaryList.size, i + chunkSize))
//
//            // 8個の要素がない場合はスキップ（必要に応じてエラー処理を追加）
//            if (chunk.size == chunkSize) {
//                val binaryString = chunk.joinToString("")
//                val decimalValue = binaryString.toInt(2) // 2進数文字列を10進数に変換
//                val hexString =
//                    decimalValue.toString(16).padStart(2, '0')
//                        .uppercase(Locale.ROOT) // 10進数を16進数に変換し、2桁で0埋めして大文字にする
//                result.add("0x$hexString")
//            } else {
//                println("警告: 8個未満の要素のチャンクをスキップしました: $chunk")
//                // 必要に応じて、このチャンクをどのように処理するか（0埋めするなど）を検討してください
//            }
//        }
//
//        return result
//    }

    private fun blackOrWhite(x: Int, y: Int, width_x: Int, bitmap_buf: Bitmap): Int {
        var inX = 0
        var inY = 0
        var blCount = 0
        var whCount = 0
        while(inX < width_x) {
            while (inY < width_x){
                val px = x * width_x + inX
                val py = y * width_x + inY
//                println("px ${px} py ${py} ")
                if (px >= 0 && px < bitmap_buf.width && py >= 0 && py < bitmap_buf.height) {
                    val colorObject = bitmap_buf[px, py]
                    //bitmapの色を取得
//                    val rr =  Color.red(colorObject)
                    val gg =  Color.green(colorObject)
//                    val bb = Color.blue(colorObject)

//                    println("r ${rr} - g ${gg} - b ${bb}")
                            if (gg > 128){ //128 255段階の半分
                                blCount++
                            } else {
                                whCount++
                            }

                } else {
//                    continue
//                    println("指定した座標はBitmapの範囲外です")
                }
                inY++
            }
            inX++
        }

        return if (blCount > whCount) {1} else {0}
    }
}