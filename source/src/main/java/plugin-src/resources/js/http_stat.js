HttpStat = {
	init : function() {
		var ds = new Ext.data.JsonStore( {
			url : 'stat/datas',
			root : 'stat',
			totalProperty : "totalCount",
			fields : [ {
				name : 'uri',
				mapping : 'key'
			}, {
				name : 'total',
				mapping : 'frequency'
			}, {
				name : 'createTime',
				mapping : 'createTime'
			}, {
				name : 'last',
				mapping : 'last'
			}, {
				name : 'min',
				mapping : 'min'
			}, {
				name : 'max',
				mapping : 'max'
			}, {
				name : 'average',
				mapping : 'average'
			}, {
				name : 'scoreGrade',
				mapping : 'scoreGrade'
			}
			],
			remoteSort : true
		});

		//ColumnModels
		var colModel = new Ext.grid.ColumnModel( [ {
			header : "URI",
			dataIndex : 'uri',
			width : 300
		}, {
			header : "开始时间",
			dataIndex : 'createTime',
			width : 140
		}, {
			header : "最后一次调用时间",
			dataIndex : 'last',
			width : 140
		}, {
			header : "最小耗时(ms)",
			dataIndex : 'min',
			sortable : true,
			width : 80
		}, {
			header : "最大耗时(ms)",
			dataIndex : 'max',
			sortable : true,
			width : 80
		}, {
			header : "平均耗时(ms)",
			dataIndex : 'average',
			sortable : true,
			width : 80
		}, {
			header : "调用次数",
			dataIndex : 'total',
			sortable : true,
			width : 60
		}, {
			header : "性能评估",
			dataIndex : 'scoreGrade',
			sortable : true,
			width : 140
		}

		]);

		var grid = new Ext.grid.Grid('grid', {
			ds : ds,
			cm : colModel,
			loadMask : true
		});

		var layout = new InfoGridLayout( {
			grid : grid,
			paging : true,
			pageSize : 25,
			displayMsg : '显示{0} - {1}条，共{2}条记录',
			emptyMsg : "暂无数据"
		});
		layout.render();

	}
};

Ext.onReady(HttpStat.init, HttpStat, true);
