package mousaiyan.pooriya.pdfcombiner

data class PDFData(
    val dir : String ,
    val name : String,
    val numberOfPages : Int,
    var chosenPageRange : MutableList<Int> = mutableListOf(1 , numberOfPages)
) {
    fun totalPageRange() = 1..numberOfPages
}
