package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.smcsmonitoringsystem.ui.theme.SMCSMonitoringSystemTheme

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String,
    data: FormControl?,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    required: Boolean = false
) {

    Column(
        modifier = modifier
    ) {

        val textLabel = buildAnnotatedString {
            append(label)
            if (required) {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                ) {
                    append(" *required")
                }
            }

        }
        Text(
            text = textLabel,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        TextField(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            value = data?.value ?: "",
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            placeholder = {Text(label)},
            onValueChange = onValueChange,
            isError = data?.dirty == true && data?.valid == false,
            supportingText = {
                val error = data?.firstError
                if (error != null)
                    Text(error)
            },
            singleLine = maxLines == 1,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )
        )
    }

}

@Preview(
    showBackground = true
)
@Composable
private fun FluxTextFieldPrev() {
    SMCSMonitoringSystemTheme {
        AppTextField(
            label = "Name",
            data = FormControl(
                initialValue = ""
            ),
            onValueChange = {}
        )
    }
}