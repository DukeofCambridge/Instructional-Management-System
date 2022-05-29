<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId+'/projects'}">项目</el-breadcrumb-item>
        <el-breadcrumb-item>对抗练习</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>
    <el-row>
      <el-card v-if="status===-1" style="margin: 18px 2%;">
        <div class="chat-window">
          <el-button type="primary"  @click="matchUser">开始匹配</el-button>
          <span style="margin-left: 60px">共5道习题，不定项选择，限时5分钟</span>
        </div>
<!--        <div class="chat-window">-->
<!--          <span>实时更新分数</span>-->
<!--          <el-input v-model="score" placeholder="新分数"/>-->
<!--          <el-button @click="userInPlay">实时更新分数</el-button>-->
<!--        </div>-->

<!--        <div class="chat-window">-->
<!--          <span>游戏结束</span>-->
<!--          <el-button @click="gameover">游戏结束</el-button>-->
<!--        </div>-->
      </el-card>
    </el-row>
    <el-dialog title="匹配中..." :visible="status===0">
      <el-button type="danger" @click="cancelMatch">取消匹配</el-button>
    </el-dialog>
    <el-row v-if="status>=1&&status<=5" >
      <el-col :span="15">
        <el-card style="margin: 18px 2%;min-height: 280px">
          <div slot="header" class="clearfix">
            <span><strong>{{status}}.{{exercises[status-1].description}}(共5道，不定项选择)</strong></span>
            <div style="height: 20px;"/>
            <span>倒计时: </span>
            <el-button type="primary" v-show="!countshow" style="width: 10%;padding: 12px 0;font-size: 13px;" disabled>{{count}} s</el-button>
          </div>
          <el-checkbox-group v-for="(o, index) in exercises[status-1].option" :key="index" v-model="check">
            <el-checkbox :label="index">{{o}}</el-checkbox>
          </el-checkbox-group>
          <el-button style="margin-top: 50px;margin-left: 45%" type="primary" @click="mark">确认</el-button>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="margin: 18px 2%;min-height: 280px">
          <div slot="header" class="clearfix">
            <span><strong>比赛实时进展</strong></span>
          </div>
          <span>您的分数:</span><el-progress :text-inside="true" :stroke-width="26" :percentage="score"></el-progress><br/>
          <span>对手一的分数:</span><el-progress :text-inside="true" :stroke-width="24" :percentage="score1" status="success"></el-progress><br/>
          <span>对手二的分数:</span><el-progress :text-inside="true" :stroke-width="24" :percentage="score2" status="exception"></el-progress><br/>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Contest",
  data(){
    return{
      name: '',
      courseId: '',
      projectId: '',

      // 匹配状态
      status: -1,

      userId: null,
      socket: null,
      score: 0,
      score1: 0,
      score2: 0,
      opponent1: null,
      connection: {
        host: '47.100.197.182',
        port: 8085,
        endpoint: '/game/match/',
        // clean: true, // 保留会话
        // connectTimeout: 4000, // 超时时间
        // reconnectPeriod: 4000, // 重连时间间隔
      },
      //计时器
      countshow:true,
      count:'',
      timer:null,
      //题目
      check:[],
      exercises:[
        {
          "answer":"2",
          "courseId":42034203,
          "description":"“软件工程的概念是为解决软件危机而提出的”这句话的意思是",
          "id":1,
          "option":"强调软件工程成功解决了软件危机的问题#说明软件危机的存在总是使软件开发不像传统工\r\n程项目那样容易管理#说明软件工程这门学科的形成是软件发展的需要#说明软件工程的概念，即：工程的原则、思想、\r\n方法可解决当时软件开发和维护存在的问题"
        },
        {
          "answer":"0",
          "courseId":42034203,
          "description":"可维护软件的主要特性包括",
          "id":2,
          "option":"可理解性、可修改性、可移植性#可使用性#可维护性、可使用性、可靠性#可测试性"
        },
        {
          "answer":"2",
          "courseId":42034203,
          "description":"CMMI能力等级中的3级是",
          "id":3,
          "option":"已执行级#已管理级#已定义级#已定量管理级"
        },
        {
          "answer":"0",
          "courseId":42034203,
          "description":"要求软件的故障率为3次／2000小时，这属于",
          "id":4,
          "option":"质量属性#性能需求#设计约束#功能需求"
        },
        {
          "answer":"1",
          "courseId":42034203,
          "description":"在哪项软件开发生命周期中，测试活动分布于整个开发生命周期？",
          "id":5,
          "option":"迭代瀑布模型#V模型#原型模型#传统瀑布模型"
        },
      ],
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.projectId = this.$route.params.projectId
    this.userId = this.$store.state.user.id
    this.connect()
    // this.handle()
    // console.log(this.exercises)
  },
  methods:{
    connect() {
      const { host, port, endpoint } = this.connection
      // let socketUrl = "ws://127.0.0.1:5003/game/match/" + this.userId;
      const socketUrl = `ws://${host}:${port}${endpoint}`+this.$store.state.user.id;
      this.socket = new WebSocket(socketUrl);
      //打开事件
      this.socket.onopen = this.onopen
      //获得消息事件
      this.socket.onmessage = this.onmessage
      //关闭事件
      this.socket.onclose = this.onclose
      //发生了错误事件
      this.socket.onerror = this.onerror
      // 加入用户
      // setTimeout(() => {
      //   this.addUser()
      // }, 1000);
    },
    onopen(){
      console.log("websocket 已打开 userId: " + this.userId);
    },
    onmessage(msg){
      console.log("收到服务端信息: ", JSON.parse(msg.data));
      let origin = JSON.parse(msg.data)
      let m = origin.chatMessage
      if(m.type==="MATCH_USER"){
        this.exercises = m.data.exercises
        this.handle()
        this.$message("匹配成功！")
        this.status=1
        this.timedown()
      }else if(m.type==="PLAY_GAME"){
        if(this.opponent1===null){
          this.opponent1 = m.data.userId
          this.score1 = m.data.score
        }else{
          if(m.data.userId===this.opponent1){
            this.score1 = m.data.score
          }else{
            this.score2 = m.data.score
          }
        }
      }else if(m.type==="GAME_OVER"){
        let re = m.data.score
        this.status = -1
        this.$message("比赛结束，您的得分为:"+re)
      }
    },
    onclose(){
      console.log("websocket 已关闭 userId: " + this.userId);
    },
    onerror(){
      console.log("websocket 发生了错误 userId : " + this.userId);
    },
    disconnect(){
      this.socket.close();
    },
    // 用户加入
    addUser(){
      let chatMessage = {};
      let sender = this.userId;
      let contestId = this.projectId;
      let type = "ADD_USER";
      chatMessage.sender = sender;
      chatMessage.type = type;
      chatMessage.contestId = contestId;
      console.log("用户:" + sender + "开始加入......");
      this.socket.send(JSON.stringify(chatMessage));
    },
    // 随机匹配
    matchUser(){
      this.addUser()
      this.status = 0//匹配中
      let chatMessage = {};
      let sender = this.userId;
      let contestId = this.projectId;
      let type = "MATCH_USER";
      chatMessage.sender = sender;
      chatMessage.type = type;
      chatMessage.contestId = contestId;
      console.log("用户:" + sender + "开始匹配......");
      this.socket.send(JSON.stringify(chatMessage));
    },
    // 取消匹配
    cancelMatch(){
      this.status=-1;
      let chatMessage = {};
      let sender = this.userId;
      let type = "CANCEL_MATCH";
      chatMessage.sender = sender;
      chatMessage.type = type;
      console.log("用户:" + sender + "取消匹配......");
      this.socket.send(JSON.stringify(chatMessage));
    },
    // 游戏中
    userInPlay(){
      let chatMessage = {};
      let sender = this.userId;
      let data = this.score
      let type = "PLAY_GAME";
      chatMessage.sender = sender;
      chatMessage.data = data;
      chatMessage.type = type;
      console.log("用户:" + sender + "更新分数为" + data);
      this.socket.send(JSON.stringify(chatMessage));
    },
    // 游戏结束
    gameover(){
      this.status = 6
      let chatMessage = {};
      let sender = this.userId;
      let type = "GAME_OVER";
      let data = this.score
      chatMessage.sender = sender;
      chatMessage.type = type;
      chatMessage.data = data
      console.log("用户:" + sender + "结束游戏");
      this.socket.send(JSON.stringify(chatMessage));
    },
    handle(){
      for(let i=0;i<this.exercises.length;i++){
        this.exercises[i].option = this.exercises[i].option.split('#')
        this.exercises[i].answer = this.exercises[i].answer.split(',')
      }
    },
    timedown(){
        // 手机号等用户输入的信息校验成功后，开始倒计时
        const TIME_COUNT = 60;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.countshow = false;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              // 时间到后强制结束
              this.countshow = true;
              clearInterval(this.timer);
              this.timer = null;
              this.userInPlay()
              this.gameover()
            }
          }, 1000)
        }
    },
    mark(){
      if(this.exercises[this.status-1].answer.toString()!==this.check.toString()){
        this.$message.error("遗憾，答错啦")
        this.score += 0
      }else{
        this.$message({
          message: "恭喜，答对啦",
          type:'success'
        })
        this.score +=20
        this.userInPlay()
      }
      this.check=[]
      this.status++
      if(this.status===6){
        // this.$message({
        //   message:"比赛结束，您可以在成绩页面查看本次比赛分数~",
        //   type:"success"
        // })
        this.gameover()
      }
    }
  }
}
</script>

<style scoped>
.chat-window{
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
