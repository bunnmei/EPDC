package space.webkombinat.epdc.Model.Controller

import android.app.PendingIntent
import android.content.Context
import android.content.Context.USB_SERVICE
import android.content.Intent
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import androidx.compose.runtime.mutableStateOf

val ACTION_USB_PERMISSION = "hogehoge"

class UsbController {

    var connected = mutableStateOf(false)

    var usbManager: UsbManager? = null
    var device: UsbDevice? = null
    var thread: Thread? = null
    var interFace: UsbInterface? = null
    var connection: UsbDeviceConnection? = null
    var endpointOut: UsbEndpoint? = null

    fun getDevice(ctx: Context) {
        usbManager = ctx.getSystemService(USB_SERVICE) as? UsbManager
        if (usbManager!!.deviceList.isNotEmpty()){
            device = usbManager?.deviceList?.values?.first()
        }
        if (device != null) {
            request_permission(ctx = ctx)
        }
    }

    fun status(): Boolean{
        if (interFace != null && endpointOut != null && connection != null) {
            return true
        }
        return false
    }
    fun transferData(array: ByteArray){
        println("data transferが呼び出されたよ")
        val packet_size_max = 64
        val time_out = 1000

        processByteArrayInBulk(array, bulkSize = packet_size_max) { bulk ->
            val ret =  connection?.bulkTransfer(endpointOut, bulk, bulk.size, time_out)
            if (ret != null) {
                if(ret < 0){
                    println("送信エラー")
                }
            }
        }
        println("送信終了")
    }

    fun setup() {
        if (device == null) {
            return
        }
        for (i in 0 until device!!.interfaceCount){
            if(device!!.getInterface(i).interfaceClass == UsbConstants.USB_CLASS_CDC_DATA) {
                interFace = device!!.getInterface(i)

                for (j in 0 until interFace!!.endpointCount){
                    if(
                        interFace!!.getEndpoint(j).type == UsbConstants.USB_ENDPOINT_XFER_BULK
                        && interFace!!.getEndpoint(j).direction == UsbConstants.USB_DIR_OUT
                    ){
                        println("endpint の発見 ${interFace!!.getEndpoint(j)}")
                        endpointOut = interFace!!.getEndpoint(j)
                    }
                }
            }
        }
        connection = usbManager!!.openDevice(device)
        connection!!.claimInterface(interFace!!, true)
        connected.value = true
    }

    fun processByteArrayInBulk(byteArray: ByteArray, bulkSize: Int = 64, processBulk: (ByteArray) -> Unit) {
        val arraySize = byteArray.size
        var startIndex = 0

        while (startIndex < arraySize) {
            val endIndex = minOf(startIndex + bulkSize, arraySize)
            val bulk = byteArray.copyOfRange(startIndex, endIndex)
            processBulk(bulk)
            startIndex += bulkSize
        }
    }

    private fun request_permission(ctx: Context) {
        val usbPermission = PendingIntent.getBroadcast(
            ctx,
            0,
            Intent(ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )

        usbManager?.requestPermission(device, usbPermission)
    }

    private fun currentStatePrint() {
        println("interFace: ${interFace}")
        println("connection: ${connection}")
        println("endpointOut: ${endpointOut}")
        println("device: ${device}")
        println("usbManager: ${usbManager}")
        println("thread: ${thread}")
    }
}