package tv.purple.monolith.models.retrofit.stv

data class Badge (
    val id: String,
    val urls: List<List<String>>,
    val users: List<String>
)