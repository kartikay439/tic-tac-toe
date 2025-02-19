package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame()
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Text(
            "TIC TAC TOE",
            style = TextStyle(
                color = Color.White,
                fontSize = 36.sp

                ),
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .height(55.dp), textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(56.dp))


        Text(
            text = when {
                winner != null -> "Winner: $winner!"
                isDraw -> "It's a Draw!"
                else -> "Turn: $currentPlayer"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    TicTacToeCell(board[i][j], winner, onClick = {
                        if (board[i][j] == "" && winner == null) {
                            board = board.toMutableList().apply { this[i][j] = currentPlayer }
                            winner = checkWinner(board)
                            isDraw =
                                board.all { row -> row.all { it.isNotEmpty() } } && winner == null
                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                        }
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            board = List(3) { MutableList(3) { "" } }
            currentPlayer = "X"
            winner = null
            isDraw = false
        },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Black
            )

        ) {
            Text("Restart Game")
        }
    }
}

@Composable
fun TicTacToeCell(value: String, winner: String?, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            value == "X" -> Color.Blue.copy(alpha = 0.3f)
            value == "O" -> Color.Red.copy(alpha = 0.3f)
            else -> Color.LightGray.copy(alpha = 0.5f)
        }, label = ""
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable { if (winner == null) onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = value, fontSize = 36.sp, fontWeight = FontWeight.Bold)
    }
}

fun checkWinner(board: List<List<String>>): String? {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty())
            return board[i][0]
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty())
            return board[0][i]
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty())
        return board[0][0]
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty())
        return board[0][2]
    return null
}
