## 显性三数    
### 介绍：一个宫、一行或一列中，三个格的候选数只包含三个数，则可从该单元里的其他格中排除那三个候选数  
12种类型：维度一3种：行、列、宫；维度二数量4种：222、223、233、333，组合后共12种，这里以行为例列举四种，其它列宫八种类似
* 基于行的222类型      
第5行中，位置[5,1]、[5,4]、[5,6]的候选数都只有2、3、6，那么2、3、6必然属于这三个位置，删除其他格包含的2、3、6
<img src="picture/obvious_triples_222_CN.png" width="825" height="645" >
* 基于行的223类型      
见图中解释         
<img src="picture/obvious_triples_223_CN.png" width="825" height="645" >
* 基于行的233类型         
见图中解释          
<img src="picture/obvious_triples_233_CN.png" width="825" height="645" >
* 基于行的333类型    
见图中解释          
<img src="picture/obvious_triples_333_CN.png" width="825" height="645" >