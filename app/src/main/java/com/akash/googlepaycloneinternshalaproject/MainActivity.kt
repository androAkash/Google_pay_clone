package com.akash.googlepaycloneinternshalaproject

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import com.google.accompanist.insets.LocalWindowInsets
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akash.googlepaycloneinternshalaproject.data.Assets
import com.akash.googlepaycloneinternshalaproject.data.GooglePay
import com.akash.googlepaycloneinternshalaproject.splashScreen.AppBarCollapsedHeight
import com.akash.googlepaycloneinternshalaproject.splashScreen.AppBarExpendedHeight
import com.akash.googlepaycloneinternshalaproject.ui.theme.*
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import java.lang.Float.max
import kotlin.math.min

class MainActivity : ComponentActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GooglePayCloneInternshalaProjectTheme {
                Surface(color = Color.White) {
                    MainFragment(Assets)
                }

            }
            navController = rememberNavController()
        }
    }
}

@Composable
fun MainFragment(googlePay: GooglePay) {
    val scrollState = rememberLazyListState()
    Box {
        Content(googlePay,scrollState)
        ParallaxToolbar(googlePay,scrollState)
    }
}

// Parallax TODO Parallax
@Composable
fun ParallaxToolbar(googlePay: GooglePay,scrollState:LazyListState) {
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOfState = with(LocalDensity.current){imageHeight.roundToPx()} - LocalWindowInsets.current.systemBars.layoutInsets.top

    val offset = min(scrollState.firstVisibleItemScrollOffset,maxOfState)

    val offSetProgress = max(0f,offset*3f-2f*maxOfState) / maxOfState

    TopAppBar(
        contentPadding = PaddingValues(), backgroundColor = White, modifier = Modifier
            .height(
                AppBarExpendedHeight
            )
            .offset { IntOffset(x = 0, y = -offset) },
        elevation = if (offset == maxOfState) 4.dp else 0.dp
    ) {
        Column {
            Box(
                Modifier
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offSetProgress
                    }) {
                Image(
                    painter = painterResource(id = R.drawable.google_illustration),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.4f, Transparent),
                                    Pair(1f, White)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Text(
                        googlePay.category,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(Shapes.small)
                            .background(Color.LightGray)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    googlePay.title,
                    color = Google,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offSetProgress).dp)
                        .scale(1f - 0.25f * offSetProgress)
                )
            }
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp)
    ) {
        CircularButton(R.drawable.back)
        CircularButton(R.drawable.google_pay_logo)
    }
}

@Composable
fun CircularButton(
    @DrawableRes iconResources: Int,
    color: Color = Google,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClick: () -> Unit = {}

) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = color),
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(painterResource(id = iconResources), null)
    }
}

// DownScreen TODO DownScreen
@Composable
fun Content(googlePay: GooglePay, scrollState: LazyListState) {
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(googlePay)
            Description(googlePay)
            UpiIdShower()
            TabHeaders()
            PeopleList(googlePay)
            BankbalanceButton()
            CheckHistoryButton()
            TransactionButton()

        }
    }
}

private fun mToast(context: Context){
    Toast.makeText(context, "This is a Sample Toast", Toast.LENGTH_LONG).show()
}

@Composable
fun TransactionButton() {
    val mContext = LocalContext.current
    Button(
        onClick = { mToast(mContext) },
        elevation = null,
        shape = Shapes.small,

        colors = ButtonDefaults.buttonColors(
            backgroundColor = Google,
            contentColor = Color.White,
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
            Text(text = "Make Payments", textAlign = TextAlign.Center,modifier = Modifier.weight(2f),
                fontWeight = FontWeight.Medium)
    }

}

@Composable
fun CheckHistoryButton() {
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LightGray,
            contentColor = Color.Black
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) { 
        Text(text = "Check your Transaction history",modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium)
        CircularButton(iconResources = R.drawable.history, elevation = null) {

        }
    }
}

@Composable
fun BankbalanceButton() {
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LightGray,
            contentColor = Color.Black
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Check your Bank balance",modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium)
        CircularButton(iconResources = R.drawable.bank, elevation = null) {

        }
    }
}

@Composable
fun TabHeaders() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("People", true, Modifier.weight(1f))
        TabButton("Businesses", false, Modifier.weight(1f))
        TabButton("Promotions", false, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) {
    Button(
        onClick = { /*TODO*/ }, shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors(
            backgroundColor = Google,
            contentColor = White
        ) else ButtonDefaults.buttonColors(backgroundColor = LightGray, contentColor = DarkGray)
    ) {
        Text(text)
    }
}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}

@Composable
fun PeopleList(googlePay: GooglePay) {
    EasyGrid(nColumns = 3, items = googlePay.people) {
        PeopleListCard(it.image, it.peopleName, Modifier)
    }
}

@Composable
fun PeopleListCard(@DrawableRes iconResources: Int, title: String, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card(
            shape = Shapes.large,
            backgroundColor = LightGray,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = iconResources),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp, fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun UpiIdShower() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 19.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .padding(horizontal = 6.dp)
    ) {

        Text(
            text = "UPI ID:akashbhattacharyak1314@okicici",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        CircularButton(iconResources = R.drawable.copy, elevation = null) {

        }
    }
}

@Composable
fun Description(googlePay: GooglePay) {
    Text(
        text = googlePay.description,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)
    )
}

@Composable
fun BasicInfo(googlePay: GooglePay) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        InfoColumn(R.drawable.barcode, googlePay.barcode)
        InfoColumn(R.drawable.selfpay, googlePay.selfPay)
        InfoColumn(R.drawable.bank, googlePay.bank)
    }
}


@Composable
fun InfoColumn(@DrawableRes iconResources: Int, text: String) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResources),
            contentDescription = null,
            tint = Google,
            modifier = Modifier.height(24.dp)
        )
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 1400)
@Composable
fun DefaultPreview() {
    GooglePayCloneInternshalaProjectTheme {
        MainFragment(Assets)
    }
}

//function for payments
private fun makePayment(
    amount: String,
    upi: String,
    name: String,
    desc: String,
    transcId: String, ctx: Context, activity: Activity, mainActivity: PaymentStatusListener
) {
    try {
        // START PAYMENT INITIALIZATION
        val easyUpiPayment = EasyUpiPayment(activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upi
            this.payeeName = name
            this.transactionId = transcId
            this.transactionRefId = transcId
            this.payeeMerchantCode = transcId
            this.description = desc
            this.amount = amount
        }
        // END INITIALIZATION

        // Register Listener for Events
        easyUpiPayment.setPaymentStatusListener(mainActivity)

        // Start payment / transaction
        easyUpiPayment.startPayment()
    } catch (e: Exception) {
        // on below line we are handling exception.
        e.printStackTrace()
        Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
    }
}