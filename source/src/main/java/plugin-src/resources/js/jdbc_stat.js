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
			header : "��ʼʱ��",
			dataIndex : 'createTime',
			width : 140
		}, {
			header : "���һ�ε���ʱ��",
			dataIndex : 'last',
			width : 140
		}, {
			header : "��С��ʱ(ms)",
			dataIndex : 'min',
			sortable : true,
			width : 80
		}, {
			header : "����ʱ(ms)",
			dataIndex : 'max',
			sortable : true,
			width : 80
		}, {
			header : "ƽ����ʱ(ms)",
			dataIndex : 'average',
			sortable : true,
			width : 80
		}, {
			header : "���ô���",
			dataIndex : 'total',
			sortable : true,
			width : 60
		}, {
			header : "��������",
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
			displayMsg : '��ʾ{0} - {1}������{2}����¼',
			emptyMsg : "��������"
		});
		layout.render();

	}
};

Ext.onReady(HttpStat.init, HttpStat, true);
