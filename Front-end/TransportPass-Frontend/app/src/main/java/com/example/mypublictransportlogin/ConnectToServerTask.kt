import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.nio.ByteBuffer
import java.util.Base64

class ConnectToServerViewModel private constructor() : ViewModel() {
    private var port = 34520
    private var serverAddress = "192.168.77.189"
    private val socket = Socket()
    private lateinit var outputStreamDeferred: OutputStream
    private lateinit var writer: PrintWriter
    private lateinit var inputStreamDeferred :InputStream
    private lateinit var reader: BufferedReader
    private val lock = Any()
    private lateinit var tip_client :String
    private var isConnected = false
    private lateinit var abonament : AbonamentDetails
    private lateinit var orar : Orar
    lateinit var ticketList: MutableList<Ticket>
    private var SIZE_TICKET :Int=-1
    private lateinit var SignupResponse : String
    private lateinit var ShowPassResponse :String
    private lateinit var PassExists : String
    private lateinit var QRDETAILS : QRDetails
    private lateinit var UpdatePasswordResponse :String
    init {
        //connectToServer()
    }

    fun set_UpdatePasswordResponse(){
        UpdatePasswordResponse=""
    }
    suspend fun getUpdatePasswordResponse() : String{
        while(!::UpdatePasswordResponse.isInitialized || UpdatePasswordResponse==""){
            delay(100)
        }
        return UpdatePasswordResponse
    }


    fun setTipClient() {
        tip_client = ""
    }

    fun setAbonament() {
        abonament = AbonamentDetails("","","",-1.00, byteArrayOf()) // Instanță implicită a clasei AbonamentDetails
    }

    fun setOrar() {
        orar = Orar(byteArrayOf()) // Instanță implicită a clasei Orar
    }

    fun setTicketList() {
        ticketList = mutableListOf()
    }

    fun setSignupResponse() {
        SignupResponse = ""
    }

    fun setShowPassResponse() {
        ShowPassResponse = ""
    }

    fun setPassExists() {
        PassExists = ""
    }

    fun resetTicketSize(){
        SIZE_TICKET=-1
    }

    companion object {
        @Volatile
        private var instance: ConnectToServerViewModel? = null

        fun getInstance(): ConnectToServerViewModel {
            return instance ?: synchronized(this) {
                instance ?: ConnectToServerViewModel().also { instance = it }
            }
        }
    }



    fun InfoQR(id: Int){
        GlobalScope.launch {
            InfoQRSuspend(id)
        }
    }




    private suspend fun ensureConnected() {
        while (!isConnected) {
            delay(100)
        }
    }



    suspend fun InfoQRSuspend(id  : Int) {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending login request")
                // Crearea unui JSONObject pentru cererea de login
                val QRInfoRequest = JSONObject().apply {
                    put("type", "QRInfo")
                    put("id", id)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(QRInfoRequest)
                writer.flush()
                Log.d("SERVER", "Sent QRInfo request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                if (type == "QRInfo") {
                    val Name = jsonResponse.getString("nume")
                    val dataExpirare = jsonResponse.getString("dataExpirare")
                    val tip = jsonResponse.getString("tip")
                    QRDETAILS =  QRDetails(Name,dataExpirare,tip)
                }
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error : ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }


    suspend fun getQRInfo() : QRDetails{
        getQRInfoSuspend()
        return QRDETAILS
    }

    fun resetQRInfo(){
        QRDETAILS.tip=""
    }

    suspend fun getQRInfoSuspend(){
        var nr =1
        while(!::QRDETAILS.isInitialized || QRDETAILS.tip==""){
            Log.d("SERVER","SUNTEM IN QRDETAILS SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }


    fun connectToServer() {
        if(!isConnected){
            GlobalScope.launch {
                try {
                    val socketAddress = InetSocketAddress(serverAddress, port)
                    socket.connect(socketAddress)
                    Log.d("SERVER", "Socket connected to $serverAddress:$port")
                    isConnected = true
                    outputStreamDeferred = socket.getOutputStream()
                    inputStreamDeferred = socket.getInputStream()
                    writer = PrintWriter(BufferedWriter(OutputStreamWriter(outputStreamDeferred)))
                    reader = BufferedReader(InputStreamReader(inputStreamDeferred))
                    Log.d("SERVER", "Socket connection completed successfully")
                    Log.d("SERVER", "Socket connection completed successfully $isConnected")
                } catch (e: Exception) {
                    Log.e("SERVER", "Socket connection failed: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }


    fun login(email: String, password: String) {
        GlobalScope.launch {
            val response = loginAsync(email, password)
        }
    }

    suspend fun loginAsync(email: String, password: String): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending login request")
                // Crearea unui JSONObject pentru cererea de login
                val loginRequest = JSONObject().apply {
                    put("type", "Login")
                    put("email", email)
                    put("parola", password)
                }
                Log.d("SERVER","REQUESTUL ESTE: $loginRequest")
                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(loginRequest)
                writer.flush()
                Log.d("SERVER", "Sent login request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER","RESPONSE IN LOGIN ESTE : $response")
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                tip_client = when (type) {  // Directly assign to `tip_client`
                    "ClientResponse" -> "CLIENT"
                    "StudentResponse" -> "STUDENT"
                    "ControlorResponse" -> "CONTROLOR"
                    else -> "EROARE"
                }
                Log.d("SERVER", "Received login response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error : ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }


    fun Verify() {
        GlobalScope.launch {
            val response = VerifyAsync()
        }
    }


    suspend fun VerifyAsync(){
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending login request")
                // Crearea unui JSONObject pentru cererea de login
                val loginRequest = JSONObject().apply {
                    put("type", "VerifyPass")
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(loginRequest)
                writer.flush()
                Log.d("SERVER", "Sent login request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received login response: $response")
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                PassExists= when (type) {  // Directly assign to `tip_client`
                    "OkResponse" -> "INTENT"
                    "ErrorResponse" -> "DISCLAIMER"
                    else -> {"NO"}
                }
                Log.d("SERVER", "Received login response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error : ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    suspend fun getVerify():String{
        getVerifySuspend()
        return PassExists
    }

    suspend fun getVerifySuspend(){
        var nr =1
        while(!::PassExists.isInitialized || PassExists == ""){
            Log.d("SERVER","SUNTEM IN Verify SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }


    suspend fun getTipClient() :String{
        Log.d("SERVER","GET TIP CLIENT")
        getTipClientSuspend()
        return tip_client
    }

    suspend fun getTipClientSuspend(){
        var nr =1
        while(!::tip_client.isInitialized || tip_client==""){

            Log.d("SERVER","SUNTEM IN TIP_CLIENT SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }


    fun updatePass(){
        GlobalScope.launch {
            val response = updatePassAsync()
        }
    }

    suspend fun updatePassAsync(){
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending BuyTicket request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "UpdateAbonament")
                }
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent UpdateAbonament request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received UpdateAbonament response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
            }
        }
    }

    fun updatePassword(email:String,password:String){
        GlobalScope.launch {
            val response = updatePasswordAsync(email,password)
        }
    }

    suspend fun updatePasswordAsync(email:String,password:String){
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending updatePassword request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "UpdatePassword")
                    put("email",email)
                    put("password",password)
                }
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent UpdatePassword request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                Log.d("SERVER", "Received UpdatePassword response: $response")
                UpdatePasswordResponse=type
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
            }
        }
    }





    fun buyTicket(tip: String, pret: Double) {
        GlobalScope.launch {
            val response = buyTicketAsync(tip, pret)
        }
    }

    suspend fun buyTicketAsync(tip: String, pret: Double): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending BuyTicket request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "BuyTicket")
                    put("pret", pret)
                    put("tip", tip)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent BuyTicket request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received buyTicket response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }




    fun buyPass(tip: String, pret: Double) {
        GlobalScope.launch {
            val response = buyPassAsync(tip, pret)
        }
    }

    suspend fun buyPassAsync(tip: String, pret: Double): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending BuyPass request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "BuyPass")
                    put("pret", pret)
                    put("tip", tip)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent BuyPass request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received buyPass response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }



    fun signup(
        firstName: String, lastName: String, email: String, CNP: String, password: String,
        statut: String,
        photo: ByteArray
    ) {
        connectToServer()
        GlobalScope.launch {
            val job = launch {signupAsync(firstName, lastName, email, CNP, password,statut,photo)}
            job.join()
        }
    }



    suspend fun signupAsync(firstName: String, lastName: String, email: String, CNP: String, password: String, statut:String, photo:ByteArray): String {
        Log.d("SERVER","PHOTE ESTE $photo")
        return withContext(Dispatchers.IO) {
            try {
                ensureConnected()
                Log.d("SERVER", "Sending signup request")

                if(statut!="Student"){


                    // Crearea unui JSONObject pentru cererea de înregistrare
                    val signupRequest = JSONObject().apply {
                        put("type", "CreateClient")
                        put("nume", firstName)
                        put("prenume", lastName)
                        put("email", email)
                        put("parola", password)
                        put("CNP", CNP)
                        put("statut", statut)
                    }

                    // Scrierea șirului JSON în fluxul de ieșire
                    writer.println(signupRequest)
                    writer.flush()
                    Log.d("SERVER", "Sent signup request")

                    // Citirea răspunsului de la server
                    val response = reader.readLine()
                    val jsonResponse = JSONObject(response)
                    val type = jsonResponse.getString("type")
                    SignupResponse=type
                    Log.d("SERVER", "Received signup response: $response")
                    response
                }
                else{
                    val base64Image = Base64.getEncoder().encodeToString(photo)
                    val VerifyPhotoRequest = JSONObject().apply {
                        put("type","VerificareStudent")
                        put("imagine",base64Image)
                    }


                    writer.println(VerifyPhotoRequest)
                    writer.flush()
                    Log.d("SERVER", "Sent VerifyPhoto request")
                    val responseVerify = reader.readLine()
                    val jsonResponseVerify = JSONObject(responseVerify)
                    val typeVerify = jsonResponseVerify.getString("type")
                    if(typeVerify=="OkResponse")
                    {
                        val signupRequest = JSONObject().apply {
                            put("type", "CreateClient")
                            put("nume", firstName)
                            put("prenume", lastName)
                            put("email", email)
                            put("parola", password)
                            put("CNP", CNP)
                            put("statut", statut)
                        }

                        // Scrierea șirului JSON în fluxul de ieșire
                        writer.println(signupRequest)
                        writer.flush()
                        Log.d("SERVER", "Sent signup request")

                        // Citirea răspunsului de la server
                        val response = reader.readLine()
                        val jsonResponse = JSONObject(response)
                        val type = jsonResponse.getString("type")
                        SignupResponse=type
                        Log.d("SERVER", "Received signup response: $response")
                        response
                    }

                    else{
                        SignupResponse="NotStudentResponse"
                        responseVerify
                    }
                }


            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }



    suspend fun getSignupResponse() :String{
        getSignupResponseSuspend()
        return SignupResponse
    }

    suspend fun getSignupResponseSuspend(){
        var nr =1
        while(!::SignupResponse.isInitialized || SignupResponse==""){

            Log.d("SERVER","SUNTEM IN SignupResponse SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }

    private fun byteArrayToJsonArray(bytes: ByteArray): String {
        return bytes.joinToString(prefix = "[", postfix = "]", transform = { it.toInt().and(0xFF).toString() })
    }


    fun show_pass() {
        GlobalScope.launch {
            showPassAsync()
        }
    }


    suspend fun showPassAsync(): String {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending ShowPass request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "ShowPass")
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent ShowPass request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER","RECEIVE SHOWPASS RESPONSE $response")
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                if(type =="AbonamentResponse"){

                    val dataIncepere = jsonResponse.getString("dataIncepere")
                    val dataExpirare = jsonResponse.getString("dataExpirare")
                    val tip = jsonResponse.getString("tip")
                    val pret = jsonResponse.getDouble("pret")
                    val qrJSONArray = jsonResponse.getJSONArray("qr")
                    val qrByteArray = ByteArray(qrJSONArray.length())
                    for (i in 0 until qrJSONArray.length()) {
                        qrByteArray[i] = qrJSONArray.getInt(i).toByte()
                    }
                    abonament=AbonamentDetails(dataIncepere,dataExpirare,tip,pret,qrByteArray)
                }
                ShowPassResponse = type
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    suspend fun getShowPassResponse() :String{
        getShowPassResponseSuspend()
        return ShowPassResponse
    }

    suspend fun getShowPassResponseSuspend(){
        var nr =1
        while(!::ShowPassResponse.isInitialized || ShowPassResponse==""){

            Log.d("SERVER","SUNTEM IN SHOWPASSRESPONSE SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }



    fun get_orar(linia:String){
        GlobalScope.launch {
            get_orar_async(linia)
        }
    }


    suspend fun get_orar_async(linia:String){
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending ORAR request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "OrarRequest")
                    put("linie",linia)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent  OrarRequest request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER","RECEIVE OrarRequest RESPONSE $response")
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                val qrJSONArray = jsonResponse.getJSONArray("imagine")
                val qrByteArray = ByteArray(qrJSONArray.length())
                for (i in 0 until qrJSONArray.length()) {
                    qrByteArray[i] = qrJSONArray.getInt(i).toByte()
                }
                orar = Orar(qrByteArray)
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    suspend fun getOrar() :Orar{
        getOrarSuspend()
        return orar
    }

    suspend fun getOrarSuspend(){
        var nr =1
        while(!::orar.isInitialized){

            Log.d("SERVER","SUNTEM IN ORAR SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }

    fun TicketRequest(){
        GlobalScope.launch{
            TicketRequestAsync()
        }
    }


    suspend fun TicketRequestAsync(){
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending GetTickets request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val getTicketsRequest = JSONObject().apply {
                    put("type", "GetTickets")
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(getTicketsRequest)
                writer.flush()
                Log.d("SERVER", "Sent  GetTickets request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER","RECEIVE GetTickets RESPONSE $response")
                val jsonResponse = JSONObject(response)
                val size = jsonResponse.getInt("size")
                SIZE_TICKET=size
                ticketList = mutableListOf()
                for (i in 0 until size) {
                    val id = jsonResponse.getInt("id$i")
                    val dataIncepere = jsonResponse.getString("dataIncepere$i")
                    val dataExpirare = jsonResponse.getString("dataExpirare$i")
                    val tip = jsonResponse.getString("tip$i")
                    val pret = jsonResponse.getDouble("pret$i")
                    val qrJSONArray = jsonResponse.getJSONArray("qr$i")
                    val qrByteArray = ByteArray(qrJSONArray.length())
                    for (i in 0 until qrJSONArray.length()) {
                        qrByteArray[i] = qrJSONArray.getInt(i).toByte()
                    }
                    var ticket = Ticket(id,dataIncepere,dataExpirare,tip,pret,qrByteArray)
                    ticketList.add(ticket)
                }
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }


    suspend fun getTicketListSuspend(){
        var nr =1
        while(!::ticketList.isInitialized ){
            Log.d("SERVER","SUNTEM IN TICKET LIST SUSPEND $nr")
            delay(100)
            nr+=1
        }
        while(ticketList.size!=SIZE_TICKET){
            Log.d("SERVER","SUNTEM IN TICKET LIST SUSPEND $nr")
            delay(100)
            nr+=1
        }
    }

    suspend fun getTicketList(): MutableList<Ticket> {
        getTicketListSuspend()
        return ticketList
    }






    suspend fun getAbonamentDetailsSuspend(){
        var nr =1
        while(!::abonament.isInitialized || abonament.pret==-1.0){

            Log.d("SERVER","SUNTEM IN ABONAMENT SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }

    suspend fun getAbonamentDetails() :AbonamentDetails{
        getAbonamentDetailsSuspend()
        return abonament
    }


}

data class Orar(
    val orar:ByteArray
)


data class Ticket(
    val id: Int,
    val dataIncepere: String,
    val dataExpirare: String,
    val tip: String,
    val pret: Double,
    val qr: ByteArray
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.createByteArray() ?: ByteArray(0)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(dataIncepere)
        parcel.writeString(dataExpirare)
        parcel.writeString(tip)
        parcel.writeDouble(pret)
        parcel.writeByteArray(qr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }
}


data class QRDetails(
    val nume :String,
    val dataExpirare :String,
    var tip: String
)


data class AbonamentDetails(
    val dataIncepere: String,
    val dataExpirare: String,
    val tip: String,
    val pret: Double,
    val qr: ByteArray
)
