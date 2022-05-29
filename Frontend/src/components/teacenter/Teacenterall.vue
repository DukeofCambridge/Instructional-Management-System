<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item>教学中心</el-breadcrumb-item>
        <el-breadcrumb-item>全校课程表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
     <!-- 卡片视图区域 -->
    <div style="height: 20px"></div>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入内容" >
            <el-button slot="append" icon="el-icon-search" ></el-button>
          </el-input>
        </el-col>
        <!-- <el-col :span="4">
          <el-button type="primary" @click="addOrderVisible = true">添加订单</el-button>
        </el-col> -->
      </el-row>
      <br/>
      <el-table :data="courses" stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="课程编号" prop="id" fit></el-table-column>
        <el-table-column label="课程名称" prop="courseName" fit></el-table-column>
        <el-table-column label="责任教师" prop="teacherName" fit></el-table-column>
        <!--        <el-table-column label="是否选课" prop="ordertype">-->
<!--          <template slot-scope="scope">-->
<!--            <el-tag type="success" v-if="scope.row.dish_order_state == '0'">已选</el-tag>-->
<!--            <el-tag type="danger" v-else-if="scope.row.dish_order_state == '1'">未选</el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>
      <span class="divcss5">总条数: {{total}}</span>
       </el-card>
  </div>
</template>
<script>
export default {
  data () {
    return {
      size: '',
      currentDate: new Date(),
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 10
      },
      total: 0,
      courses: [],
      editOrderVisible: false
    }
  },
  created() {
    this.getCourses()
  },
  methods: {
    getCourses () {
      let _this = this
      this.$axios.get('/courses/all').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.courses = resp.data.object
          _this.total = _this.courses.length
          console.log(_this.courses)
        }
      })
    },
  }
}
</script>
<style lang="less" scoped>

.el-cascader{
  width: 100%;
}
.divcss5{
  font-size: 20px;
  color:#ff9955;
}
</style>
