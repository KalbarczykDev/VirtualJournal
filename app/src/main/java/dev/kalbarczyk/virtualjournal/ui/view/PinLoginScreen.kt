package dev.kalbarczyk.virtualjournal.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kalbarczyk.virtualjournal.R
import dev.kalbarczyk.virtualjournal.model.PinUiState

@Composable
fun PinLoginScreen(
    pinUiState: PinUiState,
    pinInput: String,
    onPinInputChange: (String) -> Unit,
    onUnlockButtonClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (pinUiState.isPinSet) stringResource(R.string.enter_pin) else stringResource(R.string.set_pin),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = pinInput,
                onValueChange = onPinInputChange,
                label = { Text(stringResource(R.string.pin)) },
                isError = pinUiState.pinError,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )


            if (pinUiState.pinError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.incorrect_pin),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onUnlockButtonClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (pinUiState.isPinSet) stringResource(R.string.unlock) else stringResource(R.string.set_pin))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PinLoginScreenPreview() {
    PinLoginScreen(
        pinUiState = PinUiState(isPinSet = true, pinError = true),
        pinInput = "1234",
        onPinInputChange = {},
        onUnlockButtonClicked = {}
    )
}
