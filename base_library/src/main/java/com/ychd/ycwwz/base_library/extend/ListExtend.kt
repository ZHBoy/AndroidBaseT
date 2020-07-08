package com.ychd.ycwwz.base_library.extend

/**
 * 集合中 2 个 item 之和的平均值 组成新的数据
 */
fun <T> List<T>.twoItemAvg(transform: (T) -> Float): List<Float> {
    return mapIndexed { index, item ->
        if (index % 2 == 0) {
            if (index != size - 1) {
                (transform(item) + transform(this[index + 1])) / 2.0F
            } else {
                transform(item)
            }
        } else {
            Float.NaN
        }
    }.filter {
        !it.isNaN()
    }
}

