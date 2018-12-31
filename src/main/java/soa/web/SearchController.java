package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

  private final ProducerTemplate producerTemplate;

  @Autowired
  public SearchController(ProducerTemplate producerTemplate) {
    this.producerTemplate = producerTemplate;
  }

  @RequestMapping("/")
  public String index() {
    return "index";
  }


  @RequestMapping(value = "/search")
  @ResponseBody
  public Object search(@RequestParam("q") String q) {
    Map<String,Object> headers = new HashMap<>();
    int max;
    String words;
    try {
      String subs = q.substring(q.indexOf("max:"));
      String splt[] = subs.split(":");
      max = Integer.parseInt(splt[1]);
      words = q.substring(0,q.indexOf("max:"));
      headers.put("CamelTwitterCount",max);
    }catch(Exception e){ // There's no max
      words = q;
    }

    headers.put("CamelTwitterKeywords",words);

    return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
  }
}