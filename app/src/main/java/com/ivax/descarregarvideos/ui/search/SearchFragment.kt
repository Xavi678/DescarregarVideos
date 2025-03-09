package com.ivax.descarregarvideos.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.ui.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

//import org.openqa.selenium.chrome.ChromeDriver

//import org.openqa.selenium.WebDriver
//import org.openqa.selenium.chrome.ChromeDriver

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnSearch.setOnClickListener { view->
            var searchQuery=binding.tbxView.text
            lifecycleScope.launch {
                try {


                        //Do some Network Request
                       var httpClient= HttpClient(CIO)
                        var rsp=httpClient.post("https://www.youtube.com/youtubei/v1/search"){
                            contentType(ContentType.Application.Json)
                            setBody("{\"context\":{\"client\":{\"hl\":\"ca\",\"gl\":\"ES\",\"remoteHost\":\"46.25.76.212\",\"deviceMake\":\"\",\"deviceModel\":\"\",\"visitorData\":\"CgtDLVlyd0FVYUlPcyj0pre-BjIiCgJFUxIcEhgSFhMLFBUWFwwYGRobHB0eHw4PIBAREiEgJw%3D%3D\",\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0,gzip(gfe)\",\"clientName\":\"WEB\",\"clientVersion\":\"2.20250307.01.00\",\"osName\":\"Windows\",\"osVersion\":\"10.0\",\"originalUrl\":\"https://www.youtube.com/results?search_query=hola\",\"platform\":\"DESKTOP\",\"clientFormFactor\":\"UNKNOWN_FORM_FACTOR\",\"configInfo\":{\"appInstallData\":\"CPSmt74GEIeszhwQ-t3OHBDXwbEFEIS9zhwQt-r-EhDi1K4FEJPZzhwQ9quwBRCIh7AFENuvrwUQzN-uBRDg4P8SEM7azhwQr-XOHBC72c4cEK6P_xIQzdGxBRDT4a8FEOzezhwQkYz_EhCU_K8FEL22rgUQppqwBRCJsM4cENbYzhwQ44O4IhCZjbEFEMn3rwUQmefOHBC9irAFEI3MsAUQ8OLOHBCmmc4cEN68zhwQiOOvBRDv2c4cEOTn_xIQieiuBRD1hrEFEJ7bzhwQ6-j-EhC37f8SEODNsQUQudnOHBCdprAFEOHssAUQ_LLOHBCZmLEFEJT-sAUQy9GxBRDb2s4cEMnmsAUQ-KuxBRDerbEFEJ7QsAUQ39jOHBC45M4cEODczhwQvZmwBRCBzc4cENby_xIQ7-zOHBC3284cEJbkzhwQquHOHCooQ0FNU0dCVVRvTDJ3RE5Ia0J2UHQ4UXVQOUE3di13YjY3QU1kQnc9PQ%3D%3D\",\"coldConfigData\":\"CPSmt74GEOm6rQUQvbauBRDi1K4FEL2KsAUQntCwBRDP0rAFEOP4sAUQ9YaxBRCanLEFEKS-sQUQ18GxBRCS1LEFEKaZzhwQibDOHBCpsM4cEPayzhwQ_LLOHBCRzM4cEKnOzhwQ0dbOHBDf2M4cEM7azhwQhNvOHBCS284cEJ7bzhwQt9vOHBDg3M4cEPrdzhwQ7N7OHBC8384cEKrhzhwQ9-HOHBDw4s4cEJbkzhwQmOTOHBC45M4cEPPkzhwQr-XOHBCZ584cEIjrzhwQ7-zOHBDG7c4cEOODuCIaMkFPakZveDE2SlZnYVM2Mi1vLTc5RVlSellRYjVrSnBBYlR1cy00UFktUTU1N1cweFRRIjJBT2pGb3gyZ3gyV3RwS3oyanhWaW5YRl82UTZsTHFwMktxUkpkSWFQSVU0WjRzSEx0QSpkQ0FNU1J3MGJ1TjIzQXQ0VXpnMlhINmdxdFFTOUZmMER1c2ViRVBJVnR3T1lMUlVnbWJHM0g0V2tCWnE3QnY5WnVJQUNCT1VFcmE4RzR4R29GZDlidktvRzFqNjRiSk9OQmU4bw%3D%3D\",\"coldHashData\":\"CPSmt74GEhQxNzYyOTI4NDU3MzQ3NTczMDI2Mxj0pre-BjIyQU9qRm94MTZKVmdhUzYyLW8tNzlFWVJ6WVFiNWtKcEFiVHVzLTRQWS1RNTU3VzB4VFE6MkFPakZveDJneDJXdHBLejJqeFZpblhGXzZRNmxMcXAyS3FSSmRJYVBJVTRaNHNITHRBQmRDQU1TUncwYnVOMjNBdDRVemcyWEg2Z3F0UVM5RmYwRHVzZWJFUElWdHdPWUxSVWdtYkczSDRXa0JacTdCdjladUlBQ0JPVUVyYThHNHhHb0ZkOWJ2S29HMWo2NGJKT05CZThv\",\"hotHashData\":\"CPSmt74GEhQxMzEzNDE1NDE5NDQ0MzI1MTIwMhj0pre-BiiU5PwSKKXQ_RIonpH-EijIyv4SKLfq_hIowIP_EiiRjP8SKK6P_xIo0a7_Eiilx_8SKL3O_xIo4OD_Eijk5_8SKLft_xIo_O3_Eija7v8SKNby_xIoxPf_EijJ-f8SMjJBT2pGb3gxNkpWZ2FTNjItby03OUVZUnpZUWI1a0pwQWJUdXMtNFBZLVE1NTdXMHhUUToyQU9qRm94Mmd4Mld0cEt6Mmp4VmluWEZfNlE2bExxcDJLcVJKZElhUElVNFo0c0hMdEFCLENBTVNIUTBNb3RmNkZhN0JCcE5OOGdvVkNkM1B3Z3pHcC0wTDJNMEpwY0FG\"},\"userInterfaceTheme\":\"USER_INTERFACE_THEME_DARK\",\"timeZone\":\"Europe/Madrid\",\"browserName\":\"Firefox\",\"browserVersion\":\"136.0\",\"acceptHeader\":\"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\",\"deviceExperimentId\":\"ChxOelEzT1RnMk56QTFNekF3T1RVNE1qY3hOZz09EPSmt74GGPSmt74G\",\"rolloutToken\":\"COXojPaI8tLZpgEQptuc1YruigMYp4v34c77iwM%3D\",\"screenWidthPoints\":1920,\"screenHeightPoints\":580,\"screenPixelDensity\":1,\"screenDensityFloat\":1,\"utcOffsetMinutes\":60,\"mainAppWebInfo\":{\"graftUrl\":\"/results?search_query=hola\",\"pwaInstallabilityStatus\":\"PWA_INSTALLABILITY_STATUS_UNKNOWN\",\"webDisplayMode\":\"WEB_DISPLAY_MODE_BROWSER\",\"isWebNativeShareAvailable\":false}},\"user\":{\"lockedSafetyMode\":false},\"request\":{\"useSsl\":true,\"internalExperimentFlags\":[],\"consistencyTokenJars\":[]},\"clickTracking\":{\"clickTrackingParams\":\"CBQQ7VAiEwj_wYjIxv2LAxWZ3kkHHSUuKjE=\"},\"adSignalsInfo\":{\"params\":[{\"key\":\"dt\",\"value\":\"1741542261348\"},{\"key\":\"flash\",\"value\":\"0\"},{\"key\":\"frm\",\"value\":\"0\"},{\"key\":\"u_tz\",\"value\":\"60\"},{\"key\":\"u_his\",\"value\":\"5\"},{\"key\":\"u_h\",\"value\":\"1080\"},{\"key\":\"u_w\",\"value\":\"1920\"},{\"key\":\"u_ah\",\"value\":\"1040\"},{\"key\":\"u_aw\",\"value\":\"1920\"},{\"key\":\"u_cd\",\"value\":\"24\"},{\"key\":\"bc\",\"value\":\"31\"},{\"key\":\"bih\",\"value\":\"580\"},{\"key\":\"biw\",\"value\":\"1903\"},{\"key\":\"brdim\",\"value\":\"-8,-8,-8,-8,1920,0,1936,1056,1920,580\"},{\"key\":\"vis\",\"value\":\"1\"},{\"key\":\"wgl\",\"value\":\"true\"},{\"key\":\"ca_type\",\"value\":\"image\"}]}},\"query\":\"hola\",\"webSearchboxStatsUrl\":\"/search?oq=hola&gs_l=youtube.12..0i512k1j0i512i433k1l2j0i512i433i131k1j0i512i433k1l2j0i512i433i131k1j0i512i433k1j0i512k1j0i512i433k1j0i512k1j0i512i433k1j0i512k1j0i512i433k1...1.19260......0......................0i512i433i131i650k1.184\"}")
                        headers{
                            append(HttpHeaders.UserAgent,"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0")
                        }
                        }
                        /*val url = "https://www.youtube.com/results?search_query=${searchQuery}"
                        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
                        val inlineplayer=doc.getElementById("inline-player")*/

                        Log.d("DescarregarVides", rsp.bodyAsText())



                }catch (e: Exception){
                    Log.d("DescarregarVides", e.message.toString())
                }
            }
         }
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}