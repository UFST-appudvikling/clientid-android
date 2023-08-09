package dk.ufst.clientid

import okhttp3.Interceptor
import okhttp3.Response

class ClientIdInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
        ClientId.getRequestHeaders().forEach(modifiedRequest::addHeader)
        return chain.proceed(modifiedRequest.build())
    }
}