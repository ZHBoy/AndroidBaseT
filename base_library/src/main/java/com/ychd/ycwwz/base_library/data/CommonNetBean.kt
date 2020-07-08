package com.ychd.ycwwz.base_library.data

import java.io.Serializable

class CommonNetBean : Serializable {

    var errcode: Int = 0
    var errmsg: String? = null
    var data: Any? = null

}