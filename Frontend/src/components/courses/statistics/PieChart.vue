<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
const echarts = require('echarts');
import resize from './mixins/resize'
require('echarts/theme/macarons') // echarts theme

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data () {
    return {
      chart: null,
      data:[]
    }
  },
  mounted () {
    this.load()
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    load(){
      this.$axios.get('/score/'+this.$route.params.id+'/statistics').then(resp => {
        if (resp && resp.data.code === 200) {
          this.data = resp.data.object
          // console.log(this.data[0])
          this.$nextTick(() => {
            this.initChart()
          })
        }
      })
    },
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['<60', '60~70', '70~80', '80~90', '90+']
        },
        series: [
          {
            name: '成绩分布图',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: parseInt(this.data[0]), name: '<60' },
              { value: parseInt(this.data[1]), name: '60~70' },
              { value: parseInt(this.data[2]), name: '70~80' },
              { value: parseInt(this.data[3]), name: '80~90' },
              { value: parseInt(this.data[4]), name: '90+' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    }
  }
}
</script>
