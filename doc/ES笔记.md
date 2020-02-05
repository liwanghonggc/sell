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

post localhost:9200/nba/_doc/1  (内容选application/json)
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
		        这里可以继承指定其他文档
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
就匹配查询条件,返回该⽂档；如果不包含任意⼀个分词,表示没有任何⽂档匹配查询条件
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
```
3) 
```
