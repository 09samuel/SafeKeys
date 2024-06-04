package com.example.safekeys

import android.app.assist.AssistStructure
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillContext
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews

class MyAutofillService : AutofillService() {

    data class ParsedStructure(var usernameId: AutofillId?, var passwordId: AutofillId?)
    data class UserData(var username: String, var password: String)

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        Log.i("MyAutofillService", "onFillRequest started")

        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure
        val parsedStructure: ParsedStructure = parseStructure(structure)

        if (parsedStructure.usernameId == null || parsedStructure.passwordId == null) {
            Log.e("MyAutofillService", "ParsedStructure contains null AutofillId")
            callback.onFailure("No autofillable fields found")
            return
        }

        val userData: UserData = fetchUserData()
        Log.i("MyAutofillService", "Fetched user data: $userData")

        val usernamePresentation = RemoteViews(packageName, R.layout.autofill)
        usernamePresentation.setTextViewText(R.id.username, userData.username)
        val passwordPresentation = RemoteViews(packageName, R.layout.autofill)
        passwordPresentation.setTextViewText(R.id.label, userData.password)

        val dataset = Dataset.Builder()
            .setValue(parsedStructure.usernameId!!, AutofillValue.forText(userData.username), usernamePresentation)
            .setValue(parsedStructure.passwordId!!, AutofillValue.forText(userData.password), passwordPresentation)
            .build()

        val fillResponse = FillResponse.Builder()
            .addDataset(dataset)
            .build()

        try {
            callback.onSuccess(fillResponse)
            Log.i("MyAutofillService", "Fill response sent successfully")
        } catch (e: Exception) {
            Log.e("MyAutofillService", "Error sending fill response", e)
        }
    }

    private fun fetchUserData(): UserData {
        Log.i("MyAutofillService", "fetchUserData called")
        return UserData("my_username", "my_password")
    }

    private fun parseStructure(structure: AssistStructure): ParsedStructure {
        Log.i("MyAutofillService", "parseStructure called")
        val viewNode = structure.getWindowNodeAt(0).rootViewNode
        return ParsedStructure(viewNode.autofillId, viewNode.autofillId)
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        Log.i("MyAutofillService", "onSaveRequest started")

        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure

        traverseStructure(structure)

        callback.onSuccess()
    }

    private fun traverseStructure(structure: AssistStructure) {
        val windowNodes = structure.run { (0 until windowNodeCount).map { getWindowNodeAt(it) } }

        windowNodes.forEach { windowNode ->
            val viewNode = windowNode.rootViewNode
            traverseNode(viewNode)
        }
    }

    private fun traverseNode(viewNode: AssistStructure.ViewNode?) {
        if (viewNode == null) return

        if (viewNode.autofillHints?.isNotEmpty() == true) {
            Log.i("MyAutofillService", "Found autofill hints: ${viewNode.autofillHints?.joinToString()}")
        } else {
            Log.i("MyAutofillService", "No autofill hints found, checking view text")
        }

        val children = viewNode.run { (0 until childCount).map { getChildAt(it) } }
        children.forEach { traverseNode(it) }
    }
}
