package org.cise.core;

import android.content.Context;
import android.util.Log;

import org.cise.core.utilities.http.ApiRequest;
import org.cise.core.utilities.http.HttpError;
import org.cise.core.utilities.http.HttpRequest;
import org.cise.core.utilities.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, ApiRequest.class})
public class ExampleUnitTest {

    @Mock
    Context context;



    @Test
    public void test_http() throws Exception {
        Log.d("API-TEST", "INFO START");
        ApiRequestTest request = new ApiRequestTest(context, "https://api.digitalrecordcard.com/index.php/api_v1/login", new HttpResponse.Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("API-TEST", response);
            }

            @Override
            public void onError(HttpError httpError) {
                Log.e("API-TEST", httpError.getMessage());
            }
        });
        request.request();
        Thread.sleep(15000);
        assertEquals(4, 2 + 2);
    }
}