kafka-producers
===============

## Introduction ##

Within this repository contains Java implementations of Kafka Producers. To run the examples within the test packages, you must first have installed Kafka for Scala 2.9.2. If you are new to Kafka, I highly recommend [perusing this excellent article on installing Kafka 0.8.0 for Scala 2.9.2.]( http://www.michael-noll.com/blog/2013/03/13/running-a-multi-broker-apache-kafka-cluster-on-a-single-node/) 

## Current Implemented Producers ##
* **Simple Producer** - creates random text events and produces a message for each event.
* **Twitter Producer** - reads from the Twitter v1.1 API and produces JSON message from the tweet.

Future Planned Producers
* **RSS Feed Producer** - reads from a generic rss link and produces JSON message.
* **Web Crawler Producer** - crawls web pages and produces JSON markup of selected site information.

## Quick Run Tutorial ##

This tutorial assumes Kafka 0.8.0 for Scala 2.9.2 is installed and the user is aware of the installation directory ($KAFKA_HOME). 

### Running the SimpleProducer ###

The SimpleProducer is mimicing page visits to a website. It tracks the user's IP (used as the partitioning key), the website visited, and the date of the visit. We need to create the topic to aggregate the SimpleProducer events within. We will use the topic name *page_visits* for this tutorial. From the command line (assuming you are in $KAFKA_HOME):

```bash
bin/kafka-create-topic.sh --topic page_visits --replica 3 --zookeeper localhost:2181 --partition 5
```

Make sure you include a --partition option so you create more than one. Now run the TestSimpleProducer and data will be written to Kafka. To confirm you have data, use the command line tool to see what was written:

```bash
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic page_visits --from-beginning
```

You should see output like

```bash
1392507780074,www.example.com,192.168.2.113
1392514451547,www.example.com,192.168.2.158
1392514453616,www.example.com,192.168.2.98
1392514453636,www.example.com,192.168.2.153
1392516090909,www.example.com,192.168.2.223
1392516090930,www.example.com,192.168.2.13
1392516090951,www.example.com,192.168.2.18
1392516090954,www.example.com,192.168.2.253
```

### Running the TwitterProducer ###

The TwitterProducer takes in a *topic name* and a list of *filter keywords*. The list of *filter keywords* are used to select tweets from the Twitter Firehose that contain one or more of the (case insensitive) keywords. The tweets are then partitioned by the time the tweet was sent. This is a rather simple partitioning model. More complex partitioning models would require a rewrite of the TwitterPartition class. 

If tweets are sporadic and no specific partitioning of metadata is needed by the consumer, one should not create a partition and allow the Producer to randomly partition such as to maximize the efficiency of read/writes by distributing the load.

To run the TwitterProducer, we again must choose a topic name to aggregate the raw tweets under. The topic name we will use to aggregate tweets in this tutorial will be **tweets.live**. From the command line:

```bash
bin/kafka-create-topic.sh --topic tweets.live --replica 3 --zookeeper localhost:2181 --partition 3
```

Now run the TestTwitterProducer either by supplying your own authentication keys or utilizing the default testing keys provided. To confirm you have data, use the command line tool to see what was written:

```bash
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic tweets.live --from-beginning
```

The output will be the raw json strings of the tweets that contain one or more of your filter keywords. 

```bash
{"filter_level":"medium","contributors":null,"text":"@ArianaGrande can you follow me? ily 23","geo":null,"retweeted":false,"in_reply_to_screen_name":"ArianaGrande","truncated":false,"lang":"en","entities":{"symbols":[],"urls":[],"hashtags":[],"user_mentions":[{"id":34507480,"name":"Ariana Grande","indices":[0,13],"screen_name":"ArianaGrande","id_str":"34507480"}]},"in_reply_to_status_id_str":null,"id":434856731361832960,"source":"web","in_reply_to_user_id_str":"34507480","favorited":false,"in_reply_to_status_id":null,"retweet_count":0,"created_at":"Sun Feb 16 01:08:18 +0000 2014","in_reply_to_user_id":34507480,"favorite_count":0,"id_str":"434856731361832960","place":null,"user":{"location":"Poland","default_profile":false,"profile_background_tile":false,"statuses_count":56069,"lang":"en-gb","profile_link_color":"000000","profile_banner_url":"https://pbs.twimg.com/profile_banners/1901450630/1392398053","id":1901450630,"following":null,"protected":false,"favourites_count":6078,"profile_text_color":"333333","description":"11.4.2012 | 12.5.2012 | 2.12.2012 | 31.12.2012 | 22.1.2013 | 25.3.2013 | 26.3.2013 | 28.6.2013 | 20.8.2013 | 20.12.2013 21:47 | ask 4 fback","verified":false,"contributors_enabled":false,"profile_sidebar_border_color":"FFFFFF","name":"Tomlinson","profile_background_color":"FFFFFF","created_at":"Tue Sep 24 18:59:42 +0000 2013","is_translation_enabled":false,"default_profile_image":false,"followers_count":2587,"profile_image_url_https":"https://pbs.twimg.com/profile_images/434374987361710082/ZhvbNSnW_normal.jpeg","geo_enabled":true,"profile_background_image_url":"http://pbs.twimg.com/profile_background_images/378800000180927420/bf3LyMVA.jpeg","profile_background_image_url_https":"https://pbs.twimg.com/profile_background_images/378800000180927420/bf3LyMVA.jpeg","follow_request_sent":null,"url":null,"utc_offset":46800,"time_zone":"Nuku'alofa","notifications":null,"profile_use_background_image":true,"friends_count":2658,"profile_sidebar_fill_color":"DDEEF6","screen_name":"PierogiDlaMiley","id_str":"1901450630","profile_image_url":"http://pbs.twimg.com/profile_images/434374987361710082/ZhvbNSnW_normal.jpeg","listed_count":1,"is_translator":false},"coordinates":null}
```

## Creating New Producers ##

To create your own Producers, extend the AbstractProducer class and write in your own configuration and running methods specific to your needs. 

## Acknowledgements ##

Thanks to the [quick start guide by Apache](http://kafka.apache.org/documentation.html) for getting me started on writing producers. 
