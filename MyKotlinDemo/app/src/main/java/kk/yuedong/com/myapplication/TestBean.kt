package kk.yuedong.com.myapplication

class TestBean {

    init {

    }


    private var height = 9

    private var weight = 4


    fun getHeight(): Int {
        return height
    }

    fun setHeight(height: Int) {

        this.height = height;
    }

    override fun toString(): String {
        return "TestBean(height=$height, weight=$weight)"
    }

}