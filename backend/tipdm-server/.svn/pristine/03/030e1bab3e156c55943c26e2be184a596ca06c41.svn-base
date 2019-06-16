#coding=utf-8
import pandas as pd
import numpy as np
from sqlalchemy  import create_engine
from sqlalchemy.pool import NullPool

'''
获取数据库连接
return 数据库连接
'''
def getConnection(host, port, dbname, user, password, schema=None):
    engine = create_engine('postgresql://'+user+':'+password+'@'+host+':'+str(port)+'/'+dbname, poolclass=NullPool)
    conn = engine.connect()
    if(schema != None):
        conn.execute('set search_path='+schema)
    return conn


def closeConnection(conn):
    if(conn != None):
        conn.close()

'''
查询
conn: 数据库连接
sql: 查询语句
return pd.DataFrame
'''
def query(conn, sql):
    return pd.read_sql_query(sql, con=conn)

'''
将dataFrame插入到数据库
conn: 数据库连接
tableName: 目标表
dataFrame: 要插入的数据框
'''
def dbWriteTable(conn, tableName, dataFrame):
    if isinstance(dataFrame, pd.DataFrame) == False:
        return
    dataFrame.to_sql(tableName, index=False, if_exists='replace', con=conn)

'''
修改类型
conn:数据库连接
tableName:目标表
dataFrame:要修改的表
Type:修改类型为
'''
def modifyType(conn, tableName, dataFrame, type_dict):
    if isinstance(dataFrame, pd.DataFrame) == False:
        return
    dataFrame.to_sql(tableName, index=False, if_exists='replace',con=conn,dtype=type_dict)



