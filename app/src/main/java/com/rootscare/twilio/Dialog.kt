package com.rootscare.twilio

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.rootscare.serviceprovider.R

/**
 * Created by tconnors on 3/4/16.
 */
object Dialog {
    fun createIncomingCallDialog(
        answerCallClickListener: DialogInterface.OnClickListener?,
        cancelClickListener: DialogInterface.OnClickListener?,
        context: Context?
    ): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(
            context!!
        )
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp)
        alertDialogBuilder.setTitle("Incoming Voice Call")
        alertDialogBuilder.setPositiveButton("Accept", answerCallClickListener)
        alertDialogBuilder.setNegativeButton("Reject", cancelClickListener)
        alertDialogBuilder.setMessage("Incoming Voice call")
        return alertDialogBuilder.create()
    }

    fun createIncomingCallDialog(
        answerCallClickListener: DialogInterface.OnClickListener?,
        cancelClickListener: DialogInterface.OnClickListener?,
        waitClickListener: DialogInterface.OnClickListener?,
        context: Context?,
        name: String
    ): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(
            context!!
        )
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp)
        alertDialogBuilder.setTitle("Incoming Video Call")
        alertDialogBuilder.setPositiveButton("Accept", answerCallClickListener)
        alertDialogBuilder.setNeutralButton("Give me 5 minutes", waitClickListener)
        alertDialogBuilder.setNegativeButton("Reject", cancelClickListener)
        alertDialogBuilder.setMessage("$name calling...")
        return alertDialogBuilder.create()
    }

    fun createStudentIncomingCallDialog(
        answerCallClickListener: DialogInterface.OnClickListener?,
        cancelClickListener: DialogInterface.OnClickListener?, context: Context?, name: String
    ): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(
            context!!
        )
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp)
        alertDialogBuilder.setTitle("Incoming Video Call")
        alertDialogBuilder.setPositiveButton("Accept", answerCallClickListener)
        alertDialogBuilder.setNegativeButton("Reject", cancelClickListener)
        alertDialogBuilder.setMessage("$name calling...")
        return alertDialogBuilder.create()
    }
}