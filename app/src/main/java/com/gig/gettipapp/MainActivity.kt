package com.gig.gettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.gig.gettipapp.components.InputCurrencyField
import com.gig.gettipapp.ui.theme.GetTipAppTheme
import com.gig.gettipapp.util.calculateTotalAmount
import com.gig.gettipapp.util.calculateTotalPerPerson
import com.gig.gettipapp.util.calculateTotalTip
import com.gig.gettipapp.widgets.RoundIconButton
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                MainContent()
            }
        }
    }
}

@Composable
fun App(content: @Composable () -> Unit) {
    GetTipAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

//State Hoisting
@ExperimentalComposeUiApi
@Composable
fun MainContent() {
    val totalBill = rememberSaveable { mutableStateOf("") }
    val validCurrency = rememberSaveable(totalBill.value) {
        totalBill.value.trim().isNotEmpty() && totalBill.value.trim().isDigitsOnly()
    }
    val people = rememberSaveable { mutableStateOf(1) }
    val sliderPosition = rememberSaveable {
        mutableStateOf(0.10f)
    }

    BillForm(
        totalBill = totalBill.value,
        validCurrency = validCurrency,
        people = people.value,
        sliderPosition = sliderPosition.value,
        tipAmount = calculateTotalTip(totalBill.value, (sliderPosition.value * 100).roundToInt()),
        totalBillAmount = calculateTotalAmount(
            totalBill = totalBill.value,
            tipAmount = calculateTotalTip(totalBill.value, (sliderPosition.value * 100).roundToInt())
        ),
        totalPerPerson = calculateTotalPerPerson(
            calculateTotalAmount(
                totalBill = totalBill.value,
                tipAmount = calculateTotalTip(totalBill.value, (sliderPosition.value * 100).roundToInt())
            ),
            people.value
        ),
        onClickAdd = {
            if (people.value < 100) people.value++
        },
        onClickRemove = {
            if (people.value > 1) people.value--
        },
        onValueChangedSlider = {
            sliderPosition.value = it
        },
        onValueChangedInput = {
            totalBill.value = it
        }
    )
}

//Form Definition
@Composable
@ExperimentalComposeUiApi
fun BillForm(
    modifier: Modifier = Modifier,
    totalBill: String = "",
    validCurrency: Boolean = false,
    people: Int = 0,
    sliderPosition: Float = 0f,
    tipAmount: Double = 0.0,
    totalBillAmount: Double = 0.0,
    totalPerPerson: Double = 0.0,
    onClickAdd: () -> Unit = {},
    onClickRemove: () -> Unit = {},
    onValueChangedSlider: (Float) -> Unit = {},
    onValueChangedInput: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 20.dp)
    ) {
        TopHeader(
            totalBillAmount = totalBillAmount,
            totalPerPerson = totalPerPerson
        )
        Spacer(modifier = Modifier.height(10.dp))
        FormInputs(
            totalBill = totalBill,
            validCurrency = validCurrency,
            people = people,
            sliderPosition = sliderPosition,
            tipAmount = tipAmount,
            onClickRemove = onClickRemove,
            onClickAdd = onClickAdd,
            onValueChangedSlider = onValueChangedSlider,
            onValueChangedInput = onValueChangedInput
        )
    }
}

//Top Header containing Total Bill and Total Per Person
@Composable
fun TopHeader(totalBillAmount: Double = 0.0, totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape = CircleShape.copy(all = CornerSize(8.dp))),
        color = colorResource(R.color.purple_light)
    ) {
        Column(
            modifier = Modifier.padding(0.dp, 19.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Bill",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            // Way to format text with two decimal points
            Text(
                text = "$${"%.2f".format(totalBillAmount)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            // Way to format text with two decimal points
            Text(
                text = "$${"%.2f".format(totalPerPerson)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )
        }
    }
}

//Tip Calculation main inputs
@ExperimentalComposeUiApi
@Composable
fun FormInputs(
    modifier: Modifier = Modifier,
    totalBill: String,
    validCurrency: Boolean,
    people: Int,
    sliderPosition: Float,
    tipAmount: Double,
    onClickAdd: () -> Unit = {},
    onClickRemove: () -> Unit = {},
    onValueChangedSlider: (Float) -> Unit = {},
    onValueChangedInput: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
        ) Form@{
            InputCurrencyField(
                value = totalBill,
                labelId = "Enter Bill",
                keyboardActions = KeyboardActions {
                    if (!validCurrency) return@KeyboardActions
                    keyboardController?.hide()
                },
                onValueChange = onValueChangedInput
            )
            if (validCurrency) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Split")
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundIconButton(
                            //  modifier = Modifier.size(40.dp),
                            image = Icons.Rounded.Remove,
                            onClick = onClickRemove
                        )
                        Text(
                            text = people.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp),
                        )
                        RoundIconButton(
                            //  modifier = Modifier.size(40.dp),
                            image = Icons.Rounded.Add,
                            onClick = onClickAdd
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp), // .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(
                        text = "$${"%.2f".format(tipAmount)}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "${(sliderPosition * 100).roundToInt()}%")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        value = sliderPosition,
                        onValueChange = onValueChangedSlider,
                        onValueChangeFinished = {}
                    )
                }
                return@Form
            }
            Box(modifier = Modifier)
        }
    }
}

//Design Preview
@Preview(showBackground = true)
@ExperimentalComposeUiApi
@Composable
fun DefaultPreview() {
    App {
        MainContent()
    }
}
