import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PythonExecutor(private val interpreterPath: String) : Runnable {
    lateinit var message: String
    override fun run() {
        message =
            ProcessBuilder(interpreterPath, "-m", "timeit", "-r", "10").start().inputStream.bufferedReader().readLine()
    }
}

class MyTimerTask : Runnable {
    var secondsCounter = 0
    override fun run() {
        secondsCounter += 1
        println("Seconds passed: $secondsCounter")
    }
}


fun main(args: Array<String>) {
    println("Enter path to interpreter")
    val interpreterPath = Scanner(System.`in`).nextLine()

    val pythonExecutor = PythonExecutor(interpreterPath)
    val pythonThread = Thread(pythonExecutor)

    pythonThread.start()

    val scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(MyTimerTask(), 1, 1, TimeUnit.SECONDS)

    pythonThread.join()
    scheduler.shutdown()

    println(pythonExecutor.message)
}
