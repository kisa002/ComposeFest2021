package com.haeyum.layoutjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import coil.compose.rememberImagePainter
import com.haeyum.layoutjetpackcompose.ui.theme.LayoutJetpackComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab()
//                    SimpleList()
//                    CoroutineList()
                }
            }
        }
    }
}

enum class Type {
    SCAFFOLD,
    SIMPLE_LIST,
    WEB_IMAGE_LIST,
    COROUTINE_LIST,
    CUSTOM_LAYOUT,
    STAGGER_GRID_LAYOUT,
    CONSTRAINT_LAYOUT,
    LARGE_CONSTRAINT_LAYOUT,
    DECOUPLED_CONSTRAINT_LAYOUT,
}
val selectedType = Type.DECOUPLED_CONSTRAINT_LAYOUT

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    when(selectedType) {
        Type.CUSTOM_LAYOUT -> MyOwnColumn(modifier.padding(8.dp)) {
            Text(text = "MyOwnColumn")
            Text(text = "places items")
            Text(text = "vertically.")
            Text(text = "We've done it by hand!")
        }

        Type.STAGGER_GRID_LAYOUT ->
            Row(
                modifier = modifier
                    .background(color = Color.LightGray)
                    .padding(16.dp)
                    .size(200.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                StaggeredGrid(modifier = modifier, 5) {
                    for (topic in topics) {
                        Chip(modifier = Modifier.padding(8.dp), text = topic)
                    }
                }
            }

        Type.CONSTRAINT_LAYOUT -> ConstraintLayoutContent()
        Type.LARGE_CONSTRAINT_LAYOUT -> LargeConstraintLayout()
        Type.DECOUPLED_CONSTRAINT_LAYOUT -> DecoupledConstraintLayout()

        else -> Column(modifier = modifier.padding(8.dp)) {
            Text(text = "Hi there!")
            Text(text = "Thanks for going through the Layouts codelab")
        }
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
//            Text(text = "Item #$it")
            ImageListItem(index = it)
        }
    }
}

@Composable
fun CoroutineList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text(text = "Scroll to the top")
            }

            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scroll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(index = it)
            }
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints) // 계산

            val row = index % rows // 0 1 2 - 0 1 2 - 0 1 2
            rowWidths[row] += placeable.width // 첫 번 째 y에 width 더함
            rowHeights[row] = Math.max(rowHeights[row], placeable.height) // 기존 height vs 현재 view height 큰걸로

            Log.d("Xerakix", "row: $row")

            placeable
        }

        val width = rowWidths.maxOrNull() ?: constraints.minWidth
//        val width = rowWidths.maxOrNull()
//            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        val height = rowHeights.sumOf { it } // n 줄 height 더함
//        val height = rowHeights.sumOf { it }
//            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) { // 0번 째 줄은 Y값 0 그대로
            rowY[i] = rowY[i-1] + rowHeights[i-1] // 이전 줄 y위치에 가장 큰 height 더함
        }

        layout(width, height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows // 0 1 2 줄 순으로 삽.입
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width // 각 줄에 view width 더해줌
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Stable
fun Modifier.padding(all: Dp) =
    this.then(
        PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
    )

private class PaddingModifier(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val rtlAware: Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val horizontal = start.roundToPx() + end.roundToPx()
        val vertical = top.roundToPx() + bottom.roundToPx()

        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

        val width = constraints.constrainWidth(placeable.width + horizontal)
        val height = constraints.constrainHeight(placeable.height + vertical)

        return layout(width, height) {
            if (rtlAware)
                placeable.placeRelative(start.roundToPx(), top.roundToPx())
            else
                placeable.place(start.roundToPx(), top.roundToPx())
        }
    }
}

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text(text = "Button1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
//            centerHorizontallyTo(parent)
        })
        
        val barrier = createEndBarrier(button1, text)
        
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text(text = "Button 2")
        }
    }
}

@Composable
fun LargeConstraintLayout() {
    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(text = "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent.atLeast(100.dp)
            })
    }
}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight)
            decoupledConstraints(margin = 16.dp)
        else
            decoupledConstraints(margin = 32.dp)

        ConstraintLayout(constraints) {
            Button(
                onClick = {},
                modifier = Modifier.layoutId("button")
            ) {
                Text(text = "Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
            centerHorizontallyTo(parent)
        }
    }
}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),

            text = text1
        )

        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }
}

//@Preview
//@Composable
//fun PreviewLayoutsCodelab() {
//    LayoutJetpackComposeTheme {
//        LayoutsCodelab()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewSimpleList() {
//    LayoutJetpackComposeTheme {
//        SimpleList()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewCoroutineList() {
//    LayoutJetpackComposeTheme {
//        CoroutineList()
//    }
//}
//
//@Preview
//@Composable
//fun ChipPreview() {
//    LayoutJetpackComposeTheme {
//        Chip(text = "Hi there")
//    }
//}

@Preview
@Composable
fun LayoutPreview() {
    LayoutJetpackComposeTheme {
        BodyContent()
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    LayoutJetpackComposeTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}