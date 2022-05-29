<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId+'/projects'}">项目</el-breadcrumb-item>
        <el-breadcrumb-item>{{projectName}}</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>
    <el-row>
      <el-col :span="16">
        <el-card style="margin: 18px 2%;width: 95%;min-height: 280px">
          <div slot="header" class="clearfix">
            <span><strong>项目说明</strong></span>
            <el-button type="primary" style="float: right" @click="dialogFormVisible = true" v-if="$store.state.user.role.id===3">编辑</el-button>
          </div>
          <div class="text item">
            {{projectRequire}}
          </div>
          <div style="height: 10px"></div>
          <span>发布人: </span>
          <el-tag>{{ publisher }}</el-tag>
          <br /><br/>
          <span>截止时间: <el-date-picker
            v-model="endTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            align="right"
            readonly>
          </el-date-picker></span>
          <br /><br/>
          <span>成绩占比:
            <el-slider
            show-input
            disabled
            v-model="projectProportion">
          </el-slider></span>
          <div v-if="score!==-1">
            <div style="height: 20px" />
            <span>已评分:  {{ score }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <!--只有学生能提交作业-->
        <el-card style="margin: 18px 2%;width: 95%;min-height: 280px" v-if="$store.state.user.role.id===5">
          <el-button size="small" type="success" @click="$router.push('/courses/'+courseId+'/report/'+projectId)">在线编辑</el-button>
          <div style="height: 20px;"></div>
          <el-button
            size="small"
            type="info"
            @click="dialogUploadFileVisible = true"
          >上传文件</el-button
          >
        </el-card>
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="dialogUploadFileVisible"
      title="文件上传"
      width="15%"
      :before-close="this.handleCloseFile"
    >
      <div style="height: 20vh; padding-left: 20px">
        <el-row>
          <el-upload
            class="upload-demo"
            action="#"
            :on-change="this.checkType"
            :auto-upload="false"
            multiple
            :limit="1"
          >
            <el-button type="primary" plain>点击选择文件</el-button>
          </el-upload>
        </el-row>
        <br /><br /><br />

        <el-button type="primary" @click="this.addFile">添加</el-button>

        <br />
      </div>
    </el-dialog>
    <el-dialog
      title="项目设置"
      :visible.sync="dialogFormVisible"
      width="560px">
      <el-form label-position="right" :rules="loginFormRules" label-width="100px" class="login_form" >
        <el-form-item  prop="projectName" label="项目名称">
          <el-input v-model="projectName" ></el-input>
        </el-form-item>
        <el-form-item  prop="projectRequire" label="项目说明">
          <el-input v-model="projectRequire" type="textarea" prefix-icon="el-icon-unlock"></el-input>
        </el-form-item>
        <el-form-item prop="projectTime" label="项目截止时间">
          <el-date-picker
            v-model="endTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            align="right"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="projectProportion" label="成绩百分比">
          <el-slider
            show-input
            v-model="projectProportion">
          </el-slider>
        </el-form-item>

      </el-form>
      <!--      <date-picker></date-picker>-->
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="update">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ProjectIndex',
  data () {
    return {
      name: "",
      courseId: "",
      projectName: "",
      projectId: "",
      projectRequire: "",
      dialogFormVisible: false,
      endTime: "",
      publisher: "",
      projectProportion: 0,
      dialogUploadFileVisible: false,
      score: -1,
      userId: "",
      userName: "",
      uploadFile: "",
      loginFormRules: {
        projectName: [
          { required: true, message: "请输入项目名称", trigger: "blur" },
        ],
      },
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() < new Date();
        },
      },
    };
  },
  created() {
    this.name = this.$store.state.coursename;
    this.courseId = this.$route.params.id;
    this.projectId = this.$route.params.projectId;
    this.userId = this.$store.state.user.id;
    this.userName = this.$store.state.user.username;
    this.getInfo();
    this.getScore();
    // let cur = new Date();
    // this.out = cur<Date.parse(this.endTime)
  },
  methods: {
    editNotice (notice) {
      this.dialogFormVisible = true
      this.selectedNotice = notice
    },
    checkType(file, fileList) {
      this.uploadFile = file;
    },
    async addFile() {
      var j = parseInt(this.userName)
      var formdata = new FormData();
      formdata.append("headPic", this.uploadFile.raw);
      console.log(this.uploadFile.raw)
      formdata.append("cour_id", this.courseId);
      formdata.append("pro_id", this.projectId);
      formdata.append("stu_id", this.userId);
      await this.$axios.post("/file/upload", formdata).then((response) => {
        var res = response.data;
        console.log(response)
        if (res.code == 200) {
          this.$message.success("上传成功");
          this.uploadFile = "";
          this.dialogUploadFileVisible = false;
        }
      });
    },
    handleNodeClick (data) {
      // 通过点击叶节点来下载文件
      console.log(data)
    },
    handleRemove (file, fileList) {
      console.log(file, fileList)
    },
    handlePreview (file) {
      console.log(file)
    },
    handleExceed (files, fileList) {
      this.$alert(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    getInfo () {
      let _this = this
      this.$axios.get('/projects/'+this.projectId).then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('项目信息：', resp.data.object)
          let info = resp.data.object
          _this.projectRequire = info.projectRequire
          _this.projectName = info.projectName
          _this.endTime = info.endTime
          _this.publisher = info.publisher
          _this.projectProportion = info.projectProportion
        }
      })
    },
    getScore(){
      this.$axios.get('/score/get?uid='+this.$store.state.user.id+'&pid='+this.projectId).then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('项目信息：', resp.data.object)
          let info = resp.data.object
          if(info!=="NA"){
            this.score = info
          }
        }
      })
    },
    update(){
      let _this= this
      // console.log(this.loginForm)
      this.$axios.post('/projects/update',{
        id: this.projectId,
        projectName: this.loginForm.projectName,
        projectRequire:this.loginForm.projectRequire,
        projectProportion: this.loginForm.projectProportion,
        endTime: this.loginForm.endTime,
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert("项目发布成功！")
          this.dialogFormVisible=false
        }
      })

    }
  }
}
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}
</style>
