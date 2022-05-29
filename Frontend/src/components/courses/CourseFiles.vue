<template>
  <el-main >
    <div>
      <el-row style="margin: 18px 0px 0px 18px">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/courses/' + courseId }"
          >{{ name }} {{ courseId }}</el-breadcrumb-item
          >
          <el-breadcrumb-item>文件</el-breadcrumb-item>
        </el-breadcrumb>
      </el-row>
      <div style="height: 20px"></div>
      <br />
      <el-button type="primary" @click="dialogUploadFileVisible = true" v-if="$store.state.user.role.id===3"
      >上传文件</el-button
      >
      <br />
      <br />
      <el-row>
        <el-collapse @change="handleChange">
          <el-collapse-item
            v-for="(project, index) in this.projectList"
            :key="index"
          >
            <template v-slot:title>
              <el-tag @click="dialogUploadFileVisible = true" v-if="$store.state.user.role.id!==5" >+</el-tag>
              &nbsp;&nbsp;&nbsp;
              <el-tag type="info" style="float: right" size="mini"
              >实验ID：{{ project.id }}</el-tag>
              &nbsp;&nbsp;
              <br />
              <el-link :underline="false"> {{ project.projectName }}</el-link>
            </template>
            <br />
            <div>
              <div
                v-for="(file, index2) in projectFileList[index]"
                style="border-top: 1px solid #ccc"
                :key="index2"
              >
                <br /><br />
                <!-- <el-tag :type="primary" style="float: right" size="mini"></el-tag> -->
                <el-link
                  type="info"
                  :underline="false"
                  @click="showFile(file.id, file.filename)"
                >
                  文件名称：{{ file.filename }} &nbsp;&nbsp;&nbsp; 上传者：{{
                    file.user.name
                  }}
                </el-link>

                <el-popconfirm
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  icon-color="red"
                  title="确定删除此资料吗？"
                  @confirm="deleteFile(file.id)">
                  <template #reference>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <el-button
                      v-if="$store.state.user.role.id===3"
                      class="deletebutton"
                      type="danger"
                      size="mini"
                      plain>删除
<!--                      <el-icon><delete-filled /></el-icon>-->
                    </el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-row>
      <el-dialog :visible.sync="dialogFileVisible" width="70%">
        <iframe :src="this.fileUrl" style="width: 100%; height: 90vh"></iframe>
      </el-dialog>

      <el-dialog
        :visible.sync="dialogUploadFileVisible"
        title="文件上传"
        width="30%"
        :before-close="handleCloseFile"
      >
        <div style="height: 20vh; padding-left: 20px">
          <el-row>
            <el-upload
              class="upload-demo"
              action="#"
              :on-change="this.checkType"
              :auto-upload="false"
              multiple
              :limit="1">
              <el-button type="primary" plain>点击选择文件</el-button>
            </el-upload>
          </el-row>
          <br />
          <el-row>
            <div style="padding-bottom: 5px">请选择分类：</div>
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in projectList"
                :key="item.id"
                :label="item.projectName"
                :value="item.id"
              >
              </el-option>
            </el-select>
            &nbsp;&nbsp;&nbsp;
            <el-button type="primary" @click="addFile">添加</el-button>
          </el-row>
          <br />
        </div>
      </el-dialog>
    </div>
  </el-main>
</template>

<script>
export default {
  name: "CourseFiles",
  data() {
    return {
      name: "软件工程",
      courseId: "42034203",
      userId: "1",
      projectList: [],
      projectFileList: [],
      dialogFileVisible: false,
      loading: false,
      dialogUploadFileVisible: false,
      uploadFile: "",
      uploadProjectID: "",
      value: "",
      fileUrl: "",
    };
  },
  computed: {
    currentPath() {
      return this.$route.path;
    },
  },
  async created() {
    this.name = this.$store.state.course.courseName;
    this.courseId = this.$route.params.id;
    this.userId=this.$store.state.user.id
    this.loading = true;
    await this.getall();
    await this.geteverprojectfiles();
    this.loading = false;
    console.log(this.projectFileList);
  },
  methods: {
    handleChange(val) {
      console.log(val);
    },
    async getall() {
      let _this = this;
      await this.$axios
        .get("/projects/" + this.$route.params.id + "/all")
        .then((resp) => {
          if (resp && resp.data.code === 200) {
            console.log("当前项目：", resp.data.object);
            _this.projectList = resp.data.object;
          }
        });
    },
    async geteverprojectfiles() {
      this.projectFileList = [];
      for (let i = 0; i < this.projectList.length; i++) {
        await this.$axios
          .get("/file/" + this.projectList[i].id + "/all")
          .then((resp) => {
            if (resp && resp.data.code === 200) {
              console.log("当前项目文件：", resp.data.object);
              this.projectFileList.push(resp.data.object);
            }
          });
        // this.projectFileList.push(res)
      }
    },
    async deleteFile(fileID) {
      //  this.Visible=false
      console.log("shanchu");
      console.log(fileID);
      await this.$axios.post("/file/delete?file_id=" + fileID).then((resp) => {
        if (resp && resp.data.code === 200) {
          this.$message("删除成功！");
          this.loading = true;
          this.geteverprojectfiles();
          this.loading = false;

          // this.getCourseInfo()
          // _this.past = resp.data.object
        }
      });
    },
    async addFile() {
      //  this.Visible=false
      // console.log("shanchu");
      // console.log(this.courseId);
      // console.log(this.value);
      // console.log(this.userId);
      let formdata = new FormData();
      formdata.append("headPic", this.uploadFile.raw);
      // console.log(this.uploadFile.raw)
      formdata.append("cour_id", this.courseId);
      formdata.append("pro_id", this.value);
      formdata.append("user_id", this.userId);
      await this.$axios.post("/file/dataupload", formdata).then((response) => {
        let res = response.data;
        if (res.code === 200) {
          this.$message.success("上传成功");
          this.uploadFile = "";
          this.dialogUploadFileVisible = false;
          this.loading = true;
          this.geteverprojectfiles();
          this.loading = false;
        }
      });
    },
    async showFile(fileId, fileName) {
      console.log(fileId);
      let _this = this;
      await this.$axios.get("file/datacache/" + fileId).then((resp) => {
        if (resp && resp.data.code === 200) {
          console.log("预览成功");
        }
      });
      console.log("showfile");
      console.log(fileId);
      var importUrl = (this.fileUrl = "http://127.0.0.1:8081/" + fileName);
      this.dialogFileVisible = true;
      dialogFileVisible=true;
      //  this.fileUrl = "ftp://hnc:123@47.103.9.250/home/hnc/files/42034203/ziliao/1953060-hnc.pdf";
      var materialType = /\.[^\.]+$/.exec(importUrl);
      if (materialType !== ".pdf" && materialType !== ".txt") {
        console.log("fsdfafsafasdfafasfasfafasfafas");
        this.dialogFileVisible = false;
      }
      console.log(materialType);

      console.log(this.fileUrl);
      console.log(this.dialogFileVisible);

      await this.$axios.get("file/cache/" + fileName).then((resp) => {
        if (resp && resp.data.code === 200) {
          console.log("缓存成功");
        }
      });
    },
    handleCloseFile() {
      this.dialogUploadFileVisible = false;
    },
    checkType(file, fileList) {
      this.uploadFile = file;
    },
  },
};
</script>

<style scoped>
.deletebutton {
  color: #e0e0e0;
  /* margin-right:55%; */
  position: static;
  left: 700px;
  /* top: 20px; */
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>
