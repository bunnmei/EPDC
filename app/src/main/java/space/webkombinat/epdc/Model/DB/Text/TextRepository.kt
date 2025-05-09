package space.webkombinat.epdc.Model.DB.Text

class TextRepository(
    private val textDao: TextDao
) {
    suspend fun insertText(text: TextEntity): Long {
        textDao.create(text)
        return text.id
    }
}