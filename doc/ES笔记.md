##1、索引相关
####1.1、 创建索引

```
put localhost:9200/nba
```
返回
```
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "nba"
}
```

####1.2、查看索引
```
get localhost:9200/nba
```
返回
```
{
    "nba": {
        "aliases": {},                                -- 别名
        "mappings": {},                               -- 映射表结构
        "settings": {                                 -- 索引的设置
            "index": {
                "creation_date": "1580820851011",     -- 创建时间
                "number_of_shards": "1",              -- 分片数量
                "number_of_replicas": "1",            -- 副本数量
                "uuid": "Nh2tGIMzQuicSs-ysPMCrQ",     -- 索引唯一ID
                "version": {   
                    "created": "7020099"
                },
                "provided_name": "nba"
            }
        }
    }
}
```

####1.3、删除索引
```
delete localhost:9200/nba
```

####1.4、获取所有索引
```
get localhost:9200/_all
```
```
localhost:9200/_cat/indices?v
```
返回
```
health status index uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   nba   Nh2tGIMzQuicSs-ysPMCrQ   1   1          0            0       283b           283b
yellow open   cba   wSGktij0TTWf0K9Nx2Nhng   1   1          0            0       230b           230b
```

####1.5、判断索引是否存在
```
head localhost:9200/nba
```
返回
```
查看返回的状态码,200OK存在,404Not Found
```

####1.6、打开/关闭索引
```
post localhost:9200/nba/_close

post localhost:9200/nba/_open
```

##2、Mapping相关
####2.1、新建mapping
```
put localhost:9200/nba/_mapping  (内容选application/json)
{
	"properties": {
		"name": {
			"type": "text"
		},
		"team_name": {
			"type": "text"
		},
		"position": {
			"type": "keyword"
		},
		"play_year": {
			"type": "keyword"
		},
		"jerse_no": {
			"type": "keyword"
		}
	}
}
```

####2.2、查看mapping结构
```
get localhost:9200/nba/_mapping
```
返回
```
{
    "nba": {
        "mappings": {
            "properties": {
                "jerse_no": {
                    "type": "keyword"
                },
                "name": {
                    "type": "text"
                },
                "play_year": {
                    "type": "keyword"
                },
                "position": {
                    "type": "keyword"
                },
                "team_name": {
                    "type": "text"
                }
            }
        }
    }
}
```

####2.3、获取所有的mapping
```
get localhost:9200/_mapping

get localhost:9200/_all/_mapping
```

####2.4、修改mapping结构
```
修改一个字段的type是不行的,可以增加字段
post localhost:9200/nba/_mapping
{
	"properties": {
		"name": {
			"type": "text"
		},
		"team_name": {
			"type": "text"     -- text表示这个词可以被分词,如I am a Student
		},
		"position": {
			"type": "keyword"  -- keyword表示它是一个关键字,不可以被分词,如中国
		},
		"play_year": {
			"type": "keyword"
		},
		"jerse_no": {
			"type": "keyword"
		},
		"country": {
			"type": "keyword"
		}
	}
}
```

##3、文档的增删改查
####3.1、新增文档方式1指定id
```
ES7.x版本已经移除了type的概念,新增文档时默认type为_doc

PUT方式

put localhost:9200/nba/_doc/1  (内容选application/json)
{
 "name":"哈登",
 "team_name":"⽕箭",
 "position":"得分后卫",
 "play_year":"10",
 "jerse_no":"13",
 "country": "China"
}
```
返回
```
{
    "_index": "nba",
    "_type": "_doc",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 3
}
```
```
put localhost:9200/nba/_doc/1?op_type=create
添加文档时,若id为1的已存在,会误将原有的文档修改,这时可以在后面加上操作类型表明这是添加文档,若原文档已存在会报错
```
####3.2、新增文档方式2不指定id
```
POST方式

post localhost:9200/nba/_doc  (内容选application/json)
{
 "name":"李旺红",
 "team_name":"⽕箭",
 "position":"得分后卫",
 "play_year":"10",
 "jerse_no":"13",
 "country": "China"
}
```
返回
```
{
    "_index": "nba",
    "_type": "_doc",
    "_id": "UZONEHAB417Xste4hs7w",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 3
}
```
```
当索引不存在并且auto_create_index为true的时候,新增⽂档时会⾃动创建索引
1) 查看auto_create_index状态
   get localhost:9200/_cluster/settings
   返回
   {
       "persistent": {},
       "transient": {}
   }
   
2) 修改auto_create_index状态
   put localhost:9200/_cluster/settings
   {
      "persistent": 
      {
        "action.auto_create_index": "true"
      }
   }
   
3) 这样当创建一个文档,该文档所属的索引不存在时会自动创建索引
```

####3.3、查看文档方式1
```
get localhost:9200/nba/_doc/1
```
返回
```
{
    "_index": "nba",
    "_type": "_doc",
    "_id": "1",
    "_version": 1,
    "_seq_no": 0,
    "_primary_term": 3,
    "found": true,
    "_source": {
        "name": "哈登",
        "team_name": "⽕箭",
        "position": "得分后卫",
        "play_year": "10",
        "jerse_no": "13",
        "country": "China"
    }
}
```

####3.4、查看文档方式2
```
post localhost:9200/_mget
{
	"docs": [
		
		{
			"_index": "nba",
			"_type": "_doc",
			"_id": "1"
		},
		{
		        这里可以继续指定其他文档
		}
	]
}
```
返回
```
{
    "docs": [
        {
            "_index": "nba",
            "_type": "_doc",
            "_id": "1",
            "_version": 1,
            "_seq_no": 0,
            "_primary_term": 3,
            "found": true,
            "_source": {
                "name": "哈登",
                "team_name": "⽕箭",
                "position": "得分后卫",
                "play_year": "10",
                "jerse_no": "13",
                "country": "China"
            }
        }
    ]
}
```
```
post localhost:9200/nba/_mget
{
	"docs": [
		
		{
			"_type": "_doc",
			"_id": "1"
		},
		{
		        这里可以继承指定其他文档
		}
	]
}
```
```
post localhost:9200/nba/_doc/_mget
{
	"ids" : ["1"]
}
```
####3.5、修改文档方式1
```
post localhost:9200/nba/_update/1
{
	"doc": {
		"name": "哈登",
        "team_name": "⽕箭",
        "position": "得分后卫",
        "play_year": "10",
        "jerse_no": "13",
        "country": "America"
	}
}
```
返回
```
{
    "_index": "nba",
    "_type": "_doc",
    "_id": "1",
    "_version": 2,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 2,
    "_primary_term": 3
}
```
####3.6、修改文档方式2
```
增加字段
post localhost:9200/nba/_update/1
{
	"script": "ctx._source.age=18"
}
```
```
删除字段
post localhost:9200/nba/_update/1
{
	"script": "ctx._source.remove(\"age\")"
}
```
####3.7、修改文档方式3
```
// 修改年龄,在原来年龄上加4
post localhost:9200/nba/_update/1
{
	"script": {
		"source": "ctx._source.age += params.age",
		"params": {
			"age": 4
		}
	}
}
```
####3.8、删除文档
```
delete localhost:9200/nba/_doc/1
```

##4、搜索的简单使用
```
分为词条查询(term)和全文查询(full text)
1) 词条查询
   词条查询不会分析查询条件,只有当词条和查询字符串完全匹配时,才匹配搜索
2) 全文查询,也叫match查询
   ElasticSearch引擎会先分析查询字符串,将其拆分成多个分词,只要已分析的字段中包含词条的任意⼀个,或全部包含,
就匹配查询条件,返回该⽂档;如果不包含任意⼀个分词,表示没有任何⽂档匹配查询条件
```
####4.1、单条term查询
```
term是关键字查询,精确查询
localhost:9200/nba/_search
{
	"query":{
		"term":{
			"jerse_no":"23"
		}
	}
}
```
####4.2、多条term查询
```
多条是terms
localhost:9200/nba/_search
{
	"query":{
		"terms":{
			"jerse_no":[
				"13",
				"23"
			]
		}
	}
}
```
返回
```
{
    "took": 39,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 1,
        "hits": [
            {
                "_index": "nba",
                "_type": "_doc",
                "_id": "1",
                "_score": 1,
                "_source": {
                    "name": "哈登",
                    "team_name": "⽕箭",
                    "position": "得分后卫",
                    "play_year": 10,
                    "jerse_no": "13"
                }
            },
            {
                "_index": "nba",
                "_type": "_doc",
                "_id": "3",
                "_score": 1,
                "_source": {
                    "name": "詹姆斯",
                    "team_name": "湖⼈",
                    "position": "⼩前锋",
                    "play_year": 15,
                    "jerse_no": "23"
                }
            }
        ]
    }
}
```
####4.3、全文查询、Match查询
```
使用match查询它的字段type一定是text
查询所有,不指定分页参数的话默认只显示10条
post localhost:9200/nba/_search
{
	"query":{
		"match_all":{}
	},
	"from": 0,
	"size": 100
}
```
```
指定字段查询
post localhost:9200/nba/_search
{
	"query": {
		"match":{
			"name": "库里宝宝"
		}
	}
}
```
```
多个字段查询
post localhost:9200/nba/_search
{
	"query": {
		"multi_match":{
			"query": "shooter",
			"fields": ["name", "title"]
		}
	}
}
```
```
有点类似关键字查询,如果查后卫1就查不到了
post localhost:9200/nba/_search
{
	"query":{
		"match_phrase":{
			"position": "后卫"
		}
	}
}
```
```
前缀匹配,比如position是the best,查询the be,prefix可以查询出来,match_phrase查不出来
post localhost:9200/nba/_search
{
	"query":{
		"match_phrase_prefix":{
			"position": "后卫"
		}
	}
}
```

##5、分词器
####5.1、常用的内置分词器
```
1) standard analyzer
   标准分析器是默认分词器,如果未指定,则使⽤该分词器
2) simple analyzer
   simple分析器当它遇到只要不是字⺟的字符,就将⽂本解析成term,⽽且所有的term都是⼩写的
3) whitespace analyzer
   whitespace分析器,当它遇到空⽩字符时,就将⽂本解析成terms
4) stop analyzer
   stop分析器和simple分析器很像,唯⼀不同的是,stop分析器增加了对删除停⽌词的⽀持,默认使⽤了english停⽌词
   stopwords预定义的停⽌词列表,⽐如(the,a,an,this,of,at)等等.分词结果会删除the a is等
5) language analyzer
   特定的语⾔的分词器
6) pattern analyzer
   ⽤正则表达式来将⽂本分割成terms,默认的正则表达式是\W+(⾮单词字符)
```
####5.2、查看分词器分词结果
```
post localhost:9200/_analyze
{
    "analyzer": "standard",
    "text": "The best 3-points shooter is Curry!"
}
```
返回
```
{
    "tokens": [
        {
            "token": "the",
            "start_offset": 0,
            "end_offset": 3,
            "type": "<ALPHANUM>",
            "position": 0
        },
        {
            "token": "best",
            "start_offset": 4,
            "end_offset": 8,
            "type": "<ALPHANUM>",
            "position": 1
        },
        {
            "token": "3",
            "start_offset": 9,
            "end_offset": 10,
            "type": "<NUM>",
            "position": 2
        },
        {
            "token": "points",
            "start_offset": 11,
            "end_offset": 17,
            "type": "<ALPHANUM>",
            "position": 3
        },
        {
            "token": "shooter",
            "start_offset": 18,
            "end_offset": 25,
            "type": "<ALPHANUM>",
            "position": 4
        },
        {
            "token": "is",
            "start_offset": 26,
            "end_offset": 28,
            "type": "<ALPHANUM>",
            "position": 5
        },
        {
            "token": "curry",
            "start_offset": 29,
            "end_offset": 34,
            "type": "<ALPHANUM>",
            "position": 6
        }
    ]
}
```
####5.3、常用中文分词器的使用
```
IK分词器
1) 下载 https://github.com/medcl/elasticsearch-analysis-ik/releases对应版本
2) 安装 解压安装到plugins⽬录
3) 重启
```
```
post localhost:9200/_analyze
{
    "analyzer": "ik_max_word",
    "text": "⽕箭明年总冠军"
}
```

##6、常见的字段类型
```
数据类型分为:核心数据类型、复杂数据类型、专用数据类型
```
####6.1、核心数据类型
```
1) 字符串
   text,⽤于全⽂索引,该类型的字段将通过分词器进⾏分词
   keyword,不分词,只能搜索该字段的完整的值
   
2) 数值型
   long, integer, short, byte, double, float, half_float, scaled_float
   
3) 布尔类型
   boolean
   
4) 二进制binary
   该类型的字段把值当做经过base64编码的字符串,默认不存储,且不可搜索
   
5) 范围类型
   范围类型表示值是⼀个范围,⽽不是⼀个具体的值
   integer_range, float_range, long_range, double_range, date_range
   譬如age的类型是integer_range,那么值可以是 {"gte" : 20, "lte" : 40}. 搜索"term":{"age": 21}可以搜索该值
   
6) 日期类型
   由于Json没有date类型,所以es通过识别字符串是否符合format定义的格式来判断是否为date类型
   格式:"2022-01-01" "2022/01/01 12:10:30" 这种字符串格式
    {
        "name": "蔡x坤",
        "team_name": "勇⼠",
        "position": "得分后卫",
        "play_year": 10,
        "jerse_no": "31",
        "title": "打球最帅的明星",
        "date":"2020-01-01"
    }
    {
         "name": "杨超越",
         "team_name": "猴急",
         "position": "得分后卫",
         "play_year": 10,
         "jerse_no": "32",
         "title": "打球最可爱的明星",
         "date":1610350870
     }
```
####6.2、复杂数据类型
```
1) 数组类型Array
   ES中没有专⻔的数组类型, 直接使⽤[]定义即可,数组中所有的值必须是同⼀种数据类型, 不⽀持混合数据类型的数组:
   字符串数组 ["one", "two"]、整数数组 [ 1, 2 ]、Object对象数组[{"name": "Louis", "age": 18}, {"name": "Daniel", "age": 17}]
   同⼀个数组只能存同类型的数据,不能混存,譬如[10, "some string"]是错误的
   
2) 对象类型Object
   对象类型可能有内部对象
   {
        "name": "吴亦凡",
        "team_name": "湖⼈",
        "position": "得分后卫",
        "play_year": 10,
        "jerse_no": "33",
        "title": "最会说唱的明星",
        "date": "1641886870",
        "array": [
            "one",
            "two"
        ],
        "address": {
            "region": "China",
            "location": {
                "province": "GuangDong",
                "city": "GuangZhou"
            }
        }
   }
   
   索引方式
   "address.region": "China",
   "address.location.province": "GuangDong",
   "address.location.city": "GuangZhou"
   
   搜索
   POST localhost:9200/nba/_search
   {
        "query":{
            "match":{
                "address.region":"china"
            }
        }
   }
```

####6.3、专用数据类型
```
1) IP类型
   IP类型的字段⽤于存储IPv4或IPv6的地址,本质上是⼀个⻓整型字段
   
   修改mapping结构
   POST localhost:9200/nba/_mapping
   {
        "ip_addr": {
            "type": "ip"
        }
   }
   
   增加数据
   PUT localhost:9200/nba/_doc/9
   {
        "name": "吴亦凡",
        "team_name": "湖⼈",
        "position": "得分后卫",
        "play_year": 10,
        "jerse_no": "33",
        "title": "最会说唱的明星",
        "ip_addr": "192.168.1.1"
   }
   
   搜索
   POST localhost:9200/nba/_search
   {
    "query": {
        "term": {
             "ip_addr": "192.168.0.0/16" //(192.168.0.0~192.168.255.255)
        }
    }
   }
```

##7、玩转ElasticSearch搜索
####7.1、ES批量导入数据
```
Bulk,ES提供了⼀个叫bulk的API来进⾏批量操作

Git Bash自带有curl工具,@后面加上文件名
curl -X POST "localhost:9200/_bulk" -H 'Content-Type: application/json' --data-binary @player.txt
```

####7.2、ES之term的多种查询
```
单词级别的查询,这些查询通常⽤于结构化的数据,⽐如:number, date, keyword等,⽽不是对text.也就是说,全⽂本
查询之前要先对⽂本内容进⾏分词,⽽单词级别的查询直接在相应字段的反向索引中精确查找,单词级别的查询⼀般⽤于
数值、⽇期等类型的字段上
```
```
1) Term query精准匹配查询(查找号码为23的球员)
   POST nba/_search
   {
      "query": {
          "term": {
              "jerseyNo": "23"
           }
       }
   }
```
```
2) Exsit Query在特定的字段中查找⾮空值的⽂档(查找队名⾮空的球员)
   POST nba/_search
   {
      "query": {
          "exists": {
              "field": "teamNameEn"
          }
      }
   }
```
```
3) Prefix Query查找包含带有指定前缀term的⽂档(查找队名以Rock开头的球员)
   字段要是term类型,非text类型
   POST nba/_search
   {
     "query": {
       "prefix": {
         "teamNameEn": "Rock"
       }
     }
   }
```
```
4) Wildcard Query⽀持通配符查询,*表示任意字符,?表示任意单个字符(查找⽕箭队的球员),term查询
   POST nba/_search
   {
     "query": {
       "wildcard": {
         "teamNameEn": "Ro*s"
       }
     }
   }
```
```
5) Ids Query(查找id为1和2的球员)
   post nba/_search
   {
     "query":{
       "ids": {
         "values": [1, 2]
       }
     }
   }
```
####7.3、ES之范围查询
```
查找指定字段在指定范围内包含值(⽇期、数字或字符串)的⽂档
```
```
1) 查找在nba打了2年到10年以内的球员
   post nba/_search
   {
      "query": {
        "range": {
          "playYear": {
            "gte": 2,
            "lte": 10
          }
        }
      }
   }
```
```
2) 查找1980年到1999年出⽣的球员
   POST nba/_search
   {
      "query": {
        "range": {
          "birthDay": {
            "gte": "01/01/1980",
            "lte": "1999",
            "format": "dd/MM/yyyy||yyyy"
          }
        }
      }
   }
```
####7.4、ES的布尔查询
```
布尔查询
must       必须出现在匹配⽂档中
filter     必须出现在⽂档中,但是不打分
must_not   不能出现在⽂档中
should     应该出现在⽂档中
```
```
1)  must(查找名字叫做James的球员)
    POST nba/_search
    {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "displayNameEn": "james"
              }
            }
          ]
        }
      }
    }
```
```
2)  效果同must,但是不打分(查找名字叫做James的球员)
    POST nba/_search
    {
      "query": {
        "bool": {
          "filter": [
            {
              "match": {
                "displayNameEn": "james"
              }
            }
          ]
        }
      }
    }
```
```
3)  must_not(查找名字叫做James的⻄部球员)
    POST /nba/_search
    {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "displayNameEn": "james"
              }
            }
          ],
          "must_not": [
            {
              "term": {
                "teamConferenceEn": {
                  "value": "Eastern"
                }
              }
            }
          ]
        }
      }
    }
```
```
4)  should(查找名字叫做James的打球时间应该在11到20年⻄部球员)
    即使匹配不到也返回,只是评分不同
    POST /nba/_search
    {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "displayNameEn": "james"
              }
            }
          ],
          "must_not": [
            {
              "term": {
                "teamConferenceEn": {
                  "value": "Eastern"
                }
              }
            }
          ],
          "should": [
            {
              "range": {
                "playYear": {
                  "gte": 11,
                  "lte": 20
                }
              }
            }
          ]
        }
      }
    }
```
```
5)  如果minimum_should_match=1,则变成要查出名字叫做James的打球时间在11到20年⻄部球员
    POST /nba/_search
    {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "displayNameEn": "james"
              }
            }
          ],
          "must_not": [
            {
              "term": {
                "teamConferenceEn": {
                  "value": "Eastern"
                }
              }
            }
          ],
          "should": [
            {
              "range": {
                "playYear": {
                  "gte": 11,
                  "lte": 20
                }
              }
            }
          ],
          "minimum_should_match": 1
        }
      }
    }
```
####7.5、ES之排序查询
```
1)  ⽕箭队中按打球时间从⼤到⼩排序的球员
    POST /nba/_search
    {
      "query": {
        "match": {
          "teamNameEn": "Rockets"
        }
      },
      "sort": [
        {
          "playYear": {
            "order": "desc"
          }
        }
      ]
    }
```
```
2)  ⽕箭队中按打球时间从⼤到⼩,如果年龄相同则按照身⾼从⾼到低排序的球员
    POST /nba/_search
    {
      "query": {
        "match": {
          "teamNameEn": "Rockets"
        }
      },
      "sort": [
        {
          "playYear": {
            "order": "desc"
          },
          "heightValue": {
            "order": "desc"
          }
        }
      ]
    }
```

####7.6、ES聚合查询之指标聚合
```
聚合分析是什么?
    聚合分析是数据库中重要的功能特性,完成对⼀个查询的数据集中数据的聚合计算,如:找出某字段(或计算表达式的结果)
的最⼤值、最⼩值,计算和、平均值等. ES作为搜索引擎兼数据库,同样提供了强⼤的聚合分析能⼒.对⼀个数据集求最⼤、
最⼩、和、平均值等指标的聚合,在ES中称为指标聚合⽽关系型数据库中除了有聚合函数外,还可以对查询出的数据进⾏分
组group by,再在组上进⾏指标聚合,在ES中称为桶聚合.
```
```
1)  求出⽕箭队球员的平均年龄
    post nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "avgAge": {                -- AvgAge是自定义的
          "avg": {
            "field": "age"
          }
        }
      },
      "size": 0
    }
    
2)  value_count统计⾮空字段的⽂档数,求出⽕箭队中球员打球时间不为空的数量
    post nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "countYearNotNull": {
          "value_count": {
            "field": "playYear"
          }
        }
      },
      "size": 0
    }
    
3)  查出⽕箭队有多少名球员
    POST nba/_count
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      }
    }
    
4)  Cardinality值去重计数,查出⽕箭队中年龄不同的数量
    POST nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "countAgeDiff": {
          "cardinality": {
            "field": "age"
          }
        }
      },
      "size": 0
    }
    
5)  stats统计count max min avg sum 5个值
    POST nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "statsAge": {
          "stats": {
            "field": "age"
          }
        }
      },
      "size": 0
    }
    
6)  Extended stats⽐stats多4个统计结果: 平⽅和、⽅差、标准差、平均值加/减两个标准差的区间
    POST /nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "extendStatsAge": {
          "extended_stats": {
            "field": "age"
          }
        }
      },
      "size": 0
    }
    
7)  查出⽕箭的球员的年龄占⽐. Percentiles占⽐百分位对应的值统计,默认返回[ 1, 5, 25, 50, 75, 95, 99 ]分位上的值
    POST /nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "pecentAge": {
          "percentiles": {
            "field": "age"
          }
        }
      },
      "size": 0
    }
    
    返回
    {
      "took" : 31,
      "timed_out" : false,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      },
      "hits" : {
        "total" : {
          "value" : 21,
          "relation" : "eq"
        },
        "max_score" : null,
        "hits" : [ ]
      },
      "aggregations" : {
        "pecentAge" : {
          "values" : {
            "1.0" : 21.0,
            "5.0" : 21.0,
            "25.0" : 22.75,
            "50.0" : 25.0,
            "75.0" : 30.25,
            "95.0" : 35.349999999999994,
            "99.0" : 37.0
          }
        }
      }
    }
    
    自定义百分比
    POST /nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "percentAge": {
          "percentiles": {
            "field": "age",
            "percents": [
              20,
              50,
              75
            ]
          }
        }
      },
      "size": 0
    }
```
####7.7 ES聚合查询之桶聚合
```
关系型数据库中除了有聚合函数外,还可以对查询出的数据进⾏分组group by,再在组上进⾏指标聚合,在ES中称为桶聚合
```
```
Terms Aggregation 根据字段项分组聚合

1) ⽕箭队根据年龄进⾏分组
   POST /nba/_search
   {
     "query": {
       "term": {
         "teamNameEn": {
           "value": "Rockets"
         }
       }
     },
     "aggs": {
       "aggsAge": {
         "terms": {
           "field": "age",
           "size": 10
         }
       }
     },
     "size": 0
   }
   
2)  order分组聚合排序.⽕箭队根据年龄进⾏分组,分组信息通过年龄从⼤到⼩排序(通过指定字段)
    POST /nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "aggsAge": {
          "terms": {
            "field": "age",
            "size": 10,
            "order": {
              "_key": "desc"
            }
          }
        }
      },
      "size": 0
    }
    
3)  ⽕箭队根据年龄进⾏分组,分组信息通过⽂档数从⼤到⼩排序(通过⽂档数)
    POST /nba/_search
    {
      "query": {
        "term": {
          "teamNameEn": {
            "value": "Rockets"
          }
        }
      },
      "aggs": {
        "aggsAge": {
          "terms": {
            "field": "age",
            "size": 10,
            "order": {
              "_count": "desc"
            }
          }
        }
      },
      "size": 0
    }
    
4)  每⽀球队按该队所有球员的平均年龄进⾏分组排序(通过分组指标值)
    POST nba/_search
    {
      "aggs": {
        "aggTeamName": {
          "terms": {
            "field": "teamNameEn",
            "size": 30,
            "order": {
              "avgAge": "desc"
            }
          },
          "aggs": {
            "avgAge": {
              "avg": {
                "field": "age"
              }
            }
          }
        }
      },
      "size": 0
    }
    
5)  筛选分组聚合. 湖⼈和⽕箭队按球队平均年龄进⾏分组排序(指定值列表)
    POST nba/_search
    {
      "aggs": {
        "aggTeamName": {
          "terms": {
            "field": "teamNameEn",
            "include": [
              "Lakers",
              "Rockets",
              "Warriors"
            ],
            "exclude": [
              "Warriors"
            ],
            "size": 30,
            "order": {
              "avgAge": "desc"
            }
          },
          "aggs": {
            "avgAge": {
              "avg": {
                "field": "age"
              }
            }
          }
        }
      },
      "size": 0
    }
    
6)  Range Aggregation范围分组聚合,NBA球员年龄按20,20-35,35这样分组
    POST nba/_search
    {
      "aggs": {
        "ageRange": {
          "range": {
            "field": "age",
            "ranges": [
              {
                "to": 20
              },
              {
                "from": 20,
                "to": 35
              },
              {
                "from": 35
              }
            ]
          }
        }
      },
      "size": 0
    }
    
7)  NBA球员年龄按20,20-35,35这样分组(起别名)
    POST /nba/_search
    {
      "aggs": {
        "ageRange": {
          "range": {
            "field": "age",
            "ranges": [
              {
                "to": 20,
                "key": "A"
              },
              {
                "from": 20,
                "to": 35,
                "key": "B"
              },
              {
                "from": 35,
                "key": "C"
              }
            ]
          }
        }
      },
      "size": 0
    }
    
8)  Date Range Aggregation时间范围分组聚合
    POST /nba/_search
    {
      "aggs": {
        "birthDayRange": {
          "date_range": {
            "field": "birthDay",
            "format": "MM-yyy",
            "ranges": [
              {
                "to": "01-1989"
              },
              {
                "from": "01-1989",
                "to": "01-1999"
              },
              {
                "from": "01-1999",
                "to": "01-2009"
              },
              {
                "from": "01-2009"
              }
            ]
          }
        }
      },
      "size": 0
    }
    
9)  按天、⽉、年等进⾏聚合统计. 可按 year (1y), quarter (1q), month (1M), week (1w), day
    (1d), hour (1h), minute (1m), second (1s) 间隔聚合
    NBA球员按出⽣年分组
    POST /nba/_search
    {
      "aggs": {
        "birthday_aggs": {
          "date_histogram": {
            "field": "birthDay",
            "format": "yyyy",
            "interval": "year"
          }
        }
      },
      "size": 0
    }
```
####7.8 ES之Query_String查询
```
    query_string 查询,如果熟悉lucene的查询语法,我们可以直接⽤lucene查询语法写⼀个查询串进⾏查询,ES中接到
请求后,通过查询解析器,解析查询串⽣成对应的查询
```

##8、ES的高级使用
####8.1、ES之索引别名的引用
```
    在开发中,随着业务需求的迭代,较⽼的业务逻辑就要⾯临更新甚⾄是重构,⽽对于es来说,为了适应新的业务逻辑,
可能就要对原有的索引做⼀些修改,⽐如对某些字段做调整,甚⾄是重建索引.⽽做这些操作的时候,可能会对业务
造成影响,甚⾄是停机调整等问题.由此,es提供了索引别名来解决这些问题. 索引别名就像⼀个快捷⽅式或是软
连接,可以指向⼀个或多个索引,也可以给任意⼀个需要索引名的API来使⽤.别名的应⽤为程序提供了极⼤地灵活性
```
```
1)  创建别名
    POST /_aliases
    {
      "actions": [
        {
          "add": {
            "index": "nba",
            "alias": "nba_v1"
          }
        }
      ]
    }
    
2)  查看别名
    GET nba/_alias
    
3)  删除别名
    POST /_aliases
    {
      "actions": [
        {
          "remove": {
            "index": "nba",
            "alias": "nba_v1"
          }
        }
      ]
    }
    
4)  重命名
    POST /_aliases
    {
      "actions": [
        {
          "remove": {
            "index": "nba",
            "alias": "nba_v1.0"
          }
        },
        {
          "add": {
            "index": "nba",
            "alias": "nba_v2.0"
          }
        }
      ]
    }
    
5)  为多个索引指定一个别名
    POST /_aliases
    {
      "actions": [
        {
          "add": {
            "index": "nba",
            "alias": "national_player"
          }
        },
        {
          "add": {
            "index": "nba",
            "alias": "national_player"
          }
        }
      ]
    }
    
6)  为同个索引指定多个别名
    POST /_aliases
    {
      "actions": [
        {
          "add": {
            "index": "nba",
            "alias": "nba_v2.1"
          }
        },
        {
          "add": {
            "index": "nba",
            "alias": "nba_v2.2"
          }
        }
      ]
    }
    
7)  当别名指定了多个索引,可以指定写某个索引
    POST /_aliases
    {
      "actions": [
        {
          "add": {
            "index": "nba",
            "alias": "national_player",
            "is_write_index": true
          }
        },
        {
          "add": {
            "index": "wnba",
            "alias": "national_player"
          }
        }
      ]
    }
```
####8.2、ES之重建索引
```
    Elasticsearch是⼀个实时的分布式搜索引擎,为⽤户提供搜索服务,当我们决定存储某种数据时,在创建索引
的时候需要将数据结构完整确定下来,于此同时索引的设定和很多固定配置将⽤不能改变.当需要改变数据结构时,就
需要重新建⽴索引,为此,Elastic团队提供了很多辅助⼯具帮助开发⼈员进⾏重建索引.
```
```
步骤
1) nba取⼀个别名nba_latest, nba_latest作为对外使⽤
2) 新增⼀个索引nba_20220101,结构复制于nba索引,根据业务要求修改字段
3) 将nba数据同步到nba_20220101
4) 给nba_20220101添加别名nba_latest,删除nba别名nba_latest
5) 删除nba索引
6) 通过新别名访问索引

我们对外提供访问nba索引时使⽤的是nba_latest别名

3) 将旧索引数据copy到新索引.异步执⾏,如果reindex时间过⻓,建议加上wait_for_completion=false的参数条件,
   这样reindex将直接返回taskId
   POST /_reindex?wait_for_completion=false
   {
     "source": {
       "index": "nba"
     },
     "dest": {
       "index": "nba_20220101"
     }
   }
```

####8.3、ES之Refresh操作
```
理想的搜索:新的数据⼀添加到索引中⽴⻢就能搜索到,但是真实情况不是这样的
curl -X PUT localhost:9200/star/_doc/888 -H 'Content-Type:application/json' -d '{ "displayName": "蔡徐坤" }'
curl -X GET localhost:9200/star/_doc/_search?pretty
```
```
1) 修改默认更新时间(默认时间是1s)
   PUT /nba/_settings
   {
     "index": {
       "refresh_interval": "5s"
     }
   }
   
2) 强制刷新
   curl -X PUT localhost:9200/star/_doc/666?refresh -H 'Content-Type:application/json' -d '{ "displayName": "杨超越" }'
   curl -X GET localhost:9200/star/_doc/_search?pretty

3) 将refresh关闭
   PUT /star/_settings
   {
     "index": {
       "refresh_interval": "-1"
     }
   }
```
####8.4、ES之高亮查询
```
如果返回的结果集中很多符合条件的结果,那怎么能⼀眼就能看到我们想要的那个结果呢
```
```
1)  高亮查询
    POST nba_latest/_search
    {
      "query": {
        "match": {
          "displayNameEn": "james"
        }
      },
      "highlight": {
        "fields": {
          "displayNameEn": {}
        }
      }
    }
    
2)  自定义高亮查询
    POST /nba_latest/_search
    {
      "query": {
        "match": {
          "displayNameEn": "james"
        }
      },
      "highlight": {
        "fields": {
          "displayNameEn": {
            "pre_tags": [
              "<h1>"
            ],
            "post_tags": [
              "</h1>"
            ]
          }
        }
      }
    }
```
####8.6、ES之查询建议
```
查询建议,是为了给⽤户提供更好的搜索体验. 包括:词条检查,⾃动补全
Suggester
    Term suggester
    Phrase suggester
    Completion suggester
```
```
1)  Term suggester,term 词条建议器,对给输⼊的⽂本进⾏分词,为每个分词提供词项建议
    POST /nba_latest/_search
    {
      "suggest": {
        "my-suggestion": {
          "text": "jamse",
          "term": {
            "suggest_mode": "missing",
            "field": "displayNameEn"
          }
        }
      }
    }
    
    返回,jamse故意输错了,下面返回建议的输入值,还有打分
    {
      "took" : 5,
      "timed_out" : false,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      },
      "hits" : {
        "total" : {
          "value" : 0,
          "relation" : "eq"
        },
        "max_score" : null,
        "hits" : [ ]
      },
      "suggest" : {
        "my-suggestion" : [
          {
            "text" : "jamse",
            "offset" : 0,
            "length" : 5,
            "options" : [
              {
                "text" : "james",
                "score" : 0.8,
                "freq" : 5
              },
              {
                "text" : "jamal",
                "score" : 0.6,
                "freq" : 2
              },
              {
                "text" : "jake",
                "score" : 0.5,
                "freq" : 1
              },
              {
                "text" : "jose",
                "score" : 0.5,
                "freq" : 1
              }
            ]
          }
        ]
      }
    }
```
```
2)  Phrase suggester,phrase短语建议,在term的基础上,会考量多个term之间的关系,⽐如是否同时出现在索
    引的原⽂⾥,相邻程度,以及词频等
    POST /nba_latest/_search
    {
      "suggest": {
        "my-suggestion": {
          "text": "jamse harden",
          "phrase": {
            "field": "displayNameEn"
          }
        }
      }
    }
    
    返回
    {
      "took" : 356,
      "timed_out" : false,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      },
      "hits" : {
        "total" : {
          "value" : 0,
          "relation" : "eq"
        },
        "max_score" : null,
        "hits" : [ ]
      },
      "suggest" : {
        "my-suggestion" : [
          {
            "text" : "jamse harden",
            "offset" : 0,
            "length" : 12,
            "options" : [
              {
                "text" : "james harden",
                "score" : 0.0034703047
              },
              {
                "text" : "jamal harden",
                "score" : 0.0022665835
              },
              {
                "text" : "jake harden",
                "score" : 0.0017559344
              },
              {
                "text" : "jose harden",
                "score" : 0.0017559344
              }
            ]
          }
        ]
      }
    }
```
```
3)  Completion suggester,完成建议,要先修改teamCityEn的type为completion
    输入一个不完整的词,会返回建议你输入的完整词
    POST /nba_latest/_search
    {
      "suggest": {
        "my-suggestion": {
          "text": "Miam",
          "completion": {
            "field": "teamCityEn"
          }
        }
      }
    }
```
