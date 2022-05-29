<template>
  <div>
    <navbar />
    <div class="home_container">
      <div class="home_box">
        <!-- <div style="float: left"> -->
        <!-- <el-row> -->

        <div class="name_box" style="float: left">
          <el-date-picker
            v-model="currentDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm"
            align="right"
            readonly
          >
          </el-date-picker>
          <br /><br /><br />
          <el-descriptions
            class="margintop"
            title="个人信息"
            :column="2"
            :size="size"
          >
            <template slot="extra">
              <router-link class="link" :to="{ path: '/account' }">
                <el-button type="primary" size="small" class="left_ton">
                  进入账户中心
                </el-button>
              </router-link>
            </template>
            <el-descriptions-item label="用户ID">{{
                this.$store.state.user.username
              }}</el-descriptions-item>
            <el-descriptions-item label="用户姓名"
            >{{ this.$store.state.user.name }}
            </el-descriptions-item>
            <el-descriptions-item label="用户类型">
              <el-tag size="small">{{
                  this.$store.state.user.role.name
                }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="用户专业">
              xxxxxxx
            </el-descriptions-item>
          </el-descriptions>
          <br />
          <el-card>
            <div class="textitem">
              &nbsp;&nbsp;&nbsp;&nbsp;人生本来是苦的，苦的根源在于各种欲望。钱多了还想再多，官做大还想更大，房子宽了还想更宽，出了名还想更出名，欲望过多、过强就成了贪病。贪病犹如喝盐水，越喝越咸，越咸越要喝。当贪的欲望超越人的理性，凌驾生活的所有追求之时，就会成为阻断快乐的根源。
            </div>
          </el-card>
        </div>
        <div class="date_box" style="float: left">
          <el-calendar id="calendar">
            <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
            <template slot="dateCell" slot-scope="{ date, data }">
              <!--自定义内容-->
              <div>
                <div style="text-align: center">
                  <el-tooltip
                    v-if="brightDate.indexOf(data.day) !== -1"
                    class="item"
                    effect="dark"
                    placement="top"
                  >
                    <el-table :data=contentmy(data.day) slot="content">
                      <el-table-column width="150" property="projectName" label="项目名称">
                      <template slot-scope="scope">
                        <a @click="cancelDialog(scope.row)" style="color:blue;cursor:pointer" property="projectName">{{scope.row.projectName}}</a>
                      </template>
                      </el-table-column>
                      <el-table-column width="100" property="endTime" label="截至时间"></el-table-column>
                      <el-table-column width="100" property="projectProportion" label="项目成绩占比"></el-table-column>
                    </el-table>
                    <span class="everyDay">{{
                        data.day.split("-").slice(2).join("-")
                      }}</span>
                  </el-tooltip>
                  <span v-else>{{
                      data.day.split("-").slice(2).join("-")
                    }}</span>
                </div>
              </div>
            </template>
          </el-calendar>
        </div>

        <el-card style="margin: 18px 0; width: 100%; min-height: 280px">
          <el-row>
            <el-col
              :span="5"
              v-for="(course, index) in courses"
              :key="index"
              :offset="index % 5 !== 0 ? 1 : 0"
            >
              <el-card
                :body-style="{ padding: '20px' }"
                style="width: 250px; margin-bottom: 30px"
              >
                <img src="../assets/img/TJlogo.jpg" class="image" />
                <div style="padding: 14px; text-align: center">
                  <span
                  ><strong>{{ course.courseName }}</strong></span
                  >
                  <div class="bottom clearfix">
                    <!--                    <time class="time">{{ currentDate }}</time>-->
                    <div class="height: 10px"></div>
                    <el-row>
                      <router-link
                        class="link"
                        :to="{ path: 'courses/' + course.id }"
                      >
                        进入课程
                      </router-link>
                    </el-row>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/common/Navbar";

export default {
  components: { Navbar },
  data() {
    return {
      size: "",
      currentDate: new Date(),
      calendarData: [],
      nowpro:[],
      value: new Date(),
      courses: [],
    };
  },
  computed: {
    // 时间高亮的数组
    brightDate() {
      let ary = [];
      for (let i in this.calendarData) {
        ary.push(this.calendarData[i].endTime);
      }
      return ary;
    },
  },
  async created() {
    await this.getCourses();
    await this.getprojectList();
    console.log(
      this.$store.state.user.role.id,
      this.$store.state.user.username
    );
  },
  methods: {
    // Tooltip 文字提示
    companyCut(name){
      let company=name.split(" ")
      return company
    },
    async getprojectList() {
      for (let i = 0; i < this.courses.length; i++) {
        var temp='/projects/'+this.courses[i].id+'/all'
        await this.$axios
          .get("/projects/" + this.courses[i].id + "/all")
          .then((resp) => {
            if (resp && resp.data.code === 200) {
              for(let j=0;j<resp.data.object.length;j++){
                var tew=resp.data.object[j].endTime.toString();
                console.log('tew:',tew)
                var companyCut1 = this.companyCut(tew);
                resp.data.object[j].endTime=companyCut1[0]
                console.log('endtime:',resp.data.object[j].endTime)
                this.calendarData.push(resp.data.object[j]);

              }
            }
          });
      }
      console.log('calendarData:',this.calendarData)
      var ttt=this.calendarData[3].endTime.toString()
    },
    cancelDialog(row){
      // http://localhost:8080/courses/42034203/project/13
      var temp="/courses/"+row.course.id+"/project/"+row.id
      console.log(temp)
      this.$router.push({
        path:temp
      })
      console.log("tiao")

    },
    contentmy(date) {
      console.log("data")
      let content = "";

      var nowp=[];

      for (let i in this.calendarData) {
        if (date === this.calendarData[i].endTime) {
            nowp.push(this.calendarData[i])
        }
      }
      this.nowpro=[];
    this.nowpro=nowp;
      return nowp;
    },
    async getCourses() {
      console.log("course");
      let _this = this;
      await this.$axios.get('/courses/mine/'+this.$store.state.user.username).then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('resp_all:', resp)
          _this.courses = resp.data.object;
          console.log(_this.courses);
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.time {
  font-size: 13px;
  color: #999;
}

.textitem {
  font-size: 13px;
  color: rgb(0, 0, 0);
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  height: 200px;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.el-row {
  margin-bottom: 20px;
  padding-top: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.home_container {
  background: url("../assets/img/background.jpg") no-repeat;
  height: 120%;
  width: 100%;
  // background-size: cover;
  //background-color: #dddddd;
  position: absolute;
}

.home_box {
  width: 90%;
  height: 90%;
  background-color: #ffffff;
  opacity: 0.9;
  border-radius: 3px;
  //margin-top: 10px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .name_box {
    width: 50%;
    height: 50%;
    background-color: #ffff;
    // margin-top:10%;
    // padding: auto;
  }

  .date_box {
    width: 50%;
    height: 15%;
    background-color: #ffff;

    .is-selected {
      color: #1989fa;
    }

    /deep/ .el-calendar-table .el-calendar-day {
      height: 50%;
      width: 15%;
    }
  }

  .card_box {
    width: 100%;
    height: 100%;
    background-color: #ffff;
    // opacity: 0.85;
    // border-radius: 10%;
    position: absolute;
    // padding-top: 10%;
    // scroll-padding-top: 50%;
    // padding-top: 50%;
    margin-top: 50%;
    // padding-top: 50%;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }
}

.margintop {
  background-color: #ffff;
  margin-left: 25px;
}

.left_ton {
  margin-right: 20px;
}

// .back {
//   background-color: #dddddd;
// }

.link {
  text-decoration: none;
  color: #409eff;
}

.calender {
  // width: 430px;
  // position: relative;
  // margin: 0 auto;
  // margin-top: 20px;
  // padding: 0px;
  background-color: #ffff;
  width: 50%;
  height: 25%;
}

.everyDay {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-color: #ff4040;
  color: #fff;
  border-radius: 50%;
}
</style>
