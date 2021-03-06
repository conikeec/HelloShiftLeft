package io.shiftleft.controller;

import io.shiftleft.common.PathValidation;
import io.shiftleft.exception.InvalidCustomerRequestException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class RSSController {


    private static Logger log = LoggerFactory.getLogger(LedgerController.class);
    private static String productRssDestinationFolder = System.getenv("PRODUCT_RSS_FOLDER");

    private static Jedis jedis = new Jedis();

    @RequestMapping(value = "/rssUnsafe/{category}",
            method = RequestMethod.GET,
            produces = "application/rss+xml")
    public HttpServletResponse getRssForCategoryUnsafe(@PathVariable("categoryName") String categoryName, HttpServletResponse response ) throws IOException {

        InputStream inputStream = null;

        StringBuffer filePath = new StringBuffer(productRssDestinationFolder);
        filePath.append(categoryName);
        filePath.append(".xml");

        try {
            inputStream = new FileInputStream(filePath.toString());
            OutputStream out = response.getOutputStream();
            int nextChar;
            while ((nextChar = inputStream.read()) != -1) {
                out.write(nextChar);
            }

        } catch (Exception e){
            log.error("Exception caught in getRssForCategory " + e.getLocalizedMessage());
        }
        return response;

    }


    @RequestMapping(value = "/rssSafe/{category}",
            method = RequestMethod.GET,
            produces = "application/rss+xml")
    public HttpServletResponse getRssForCategorySafe(@PathVariable("categoryName") String categoryName, HttpServletResponse response ) throws IOException {

        InputStream inputStream = null;
        PathValidation pathValidation = new PathValidation();

        StringBuffer filePath = new StringBuffer(productRssDestinationFolder);
        filePath.append(categoryName);
        filePath.append(".xml");

        try {

            String validatedFeed = pathValidation.validateRssFeed(filePath.toString());
            inputStream = new FileInputStream(validatedFeed);
            OutputStream out = response.getOutputStream();
            int nextChar;
            while ((nextChar = inputStream.read()) != -1) {
                out.write(nextChar);
            }

        } catch (Exception e) {
            log.error("Exception caught in getRssForCategory " + e.getLocalizedMessage());
        }

        return response;

    }



}
