<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item>成绩</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>

     <!-- 卡片视图区域 -->
    <div style="height: 20px"></div>
    <el-card style="margin: 18px 2%;width: 95%">
<!--      <el-row v-if="this.$store.state.user.role.id===3" :gutter="20">-->
<!--        <el-button style="margin-left: 20px" type="primary" @click="tostatistics">查看成绩统计报告</el-button>-->
<!--      </el-row>-->
      <div v-if="this.$store.state.user.role.id===3" :gutter="20">
        <pie-chart ref="chart"></pie-chart>
      </div>
      <br/>
      <el-table :data="scores" stripe>
<!--        <el-table-column type="index" label="序号" fit></el-table-column>-->
        <el-table-column label="学号" prop="username" fit sortable></el-table-column>
        <el-table-column label="姓名" prop="name" fit></el-table-column>
        <el-table-column v-for="(item, index) in past" :key="index" :label="item.projectName"  fit>
          <template slot-scope="scope">
            <span style="margin-right: 10px">{{scope.row.grades[index]===-1? '未批改':scope.row.grades[index]}}</span>
            <el-tooltip  v-if="item.type===2" effect="dark" content= "下载学生作业" placement="top" :enterable="false">
              <el-button size="mini" type="primary" circle icon="el-icon-download" @click="projectId=item.id;username=scope.row.username;download()"></el-button>
            </el-tooltip>
            <el-tooltip  v-if="item.type===0" effect="dark" content= "查看学生报告" placement="top" :enterable="false">
              <el-button size="mini" type="primary" circle icon="el-icon-search" @click="projectId=item.id;username=scope.row.username;getReport()"></el-button>
            </el-tooltip>
            <el-tooltip  v-if="item.type!==1&&role!==5" effect="dark" content= "评分" placement="top" :enterable="false">
              <el-button size="mini" type="success" circle icon="el-icon-edit" @click="Visible=true;username=scope.row.username;projectId=item.id"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="当前总成绩" prop="overall" fit sortable></el-table-column>
      </el-table>
      <span class="divcss5" v-if="this.$store.state.user.role.id!==5">总条数:{{total}}</span>
     </el-card>

      <el-dialog title="项目评分"  :visible.sync="Visible" width="30%" @close="Visible = false">
      <!-- 内容主体区域 -->
        <el-form label-width="70px">
          <el-form-item label="作业评分">
            <el-slider
              show-input
              :max="100"
              v-model="mark">
            </el-slider>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <span slot="footer" class="dialog-footer">
          <el-button @click="mark=0;Visible = false">取 消</el-button>
          <el-button type="primary" @click="marks">确 定</el-button>
        </span>
      </el-dialog>
  </div>
</template>

<script>
import PieChart from "@/components/courses/statistics/PieChart";
export default {
  name: 'CourseGrades',
  components: {PieChart},
  data () {
    return {
      name: '',
      courseId: '',
      scores: [],
      past: [],
      total: 0,
      role: 5,

      // 批分表单
      Visible: false,
      mark: 0,
      username: '',
      projectId: 0,
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.role = this.$store.state.user.role.id
    this.getPast()
    this.getCourseInfo()
  },
  methods:{
    getCourseInfo () {
      let _this = this
      this.$axios.get('/score/'+this.$route.params.id).then(resp => {
        if (resp && resp.data.code === 200) {
          // console.log('课程成绩信息：', resp.data.object)
          this.scores = resp.data.object
          this.total = resp.data.object.length
          // 学生只能看自己成绩
          if(this.$store.state.user.role.id===5){
            for(let i =0;i<this.scores.length;i++){
              if(this.scores[i].username===_this.$store.state.user.username){
                this.scores=this.scores.slice(i,i+1)
                console.log(this.scores)
                break
              }
            }
          }
        }
      })
    },
    async download(){
      // console.log("shanchu");
      // console.log(this.courseId);
      // console.log(this.projectId);
      // console.log(this.username);
      var formdata = new FormData();

      formdata.append("cour_id", this.courseId);
      formdata.append("pro_id", this.projectId);
      formdata.append("stu_name", this.username);
      await this.$axios.post("/file/download", formdata).then((response) => {
        var res = response.data;
        console.log(res)
        if (res.code === 200) {
          this.$message.success("文件已下载至'D://download'目录");
        }
        else{
          this.$message.error("下载失败");
        }
      });
    },
    getPast () {
      let _this = this
      this.$axios.get('/projects/'+this.$route.params.id+'/all').then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('所有项目：', resp.data.object)
          _this.past = resp.data.object
        }
      })
    },
    marks(){
      // console.log(this.username,this.projectId)
      this.Visible=false
      this.$axios.post('/score/mark?username='+this.username+'&projectId='+this.projectId+'&score='+this.mark).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message("评分成功！")
          this.getCourseInfo()
          this.$refs.chart.load()
          // _this.past = resp.data.object
        }
      })
    },
    tostatistics(){
      this.$router.push('/courses/'+this.$route.params.id+'/statistics')
    },
    getReport(){
      this.$router.push('/courses/'+this.$route.params.id+'/report/'+this.projectId+'/'+this.username)
    }
  }
}
</script>

<style scoped>
.divcss5{
  font-size: 20px;
  color:#ff9955;
}
</style>
